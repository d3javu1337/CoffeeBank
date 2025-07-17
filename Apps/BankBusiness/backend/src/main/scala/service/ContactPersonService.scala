package service

import dao.repository.{BusinessClientRepository, ContactPersonRepository, ContactPersonRepositoryLive}
import dto.contactperson.{ContactPersonCreateDto, ContactPersonReadDto, ContactPersonUpdateDto}
import errors.NoEntityPresented
import kafka.ProducerService
import kafka.messages.contactperson.{ContactPersonCreateRequest, ContactPersonDeleteRequest, ContactPersonUpdateRequest}
import model.ContactPerson
import zio.{RIO, RLayer, Task, ZIO, ZLayer}

case class ContactPersonService(
                                 private val contactPersonRepository: ContactPersonRepository,
                                 private val businessClientService: BusinessClientService) {

  def getContactPersons(businessClientEmail: String): RIO[BusinessClientRepository, List[ContactPersonReadDto]] = for {
    id <- businessClientService.getIdByEmail(businessClientEmail)
    contactPersons <- contactPersonRepository.findAllByBusinessClientId(id)
  } yield contactPersons

  def getContactPersonById(personId: Long): Task[Option[ContactPerson]] =
    contactPersonRepository.findById(personId)

  def getContactPersonDtoByEmail(businessClientEmail: String, personId: Long): RIO[BusinessClientRepository, ContactPersonReadDto] =
    (for {
      id <- businessClientService.getIdByEmail(businessClientEmail)
      dto <- contactPersonRepository.findDtoById(id, personId)
    } yield (dto))
      .someOrFail(NoEntityPresented())

  def createContactPerson(businessClientEmail: String, dto: ContactPersonCreateDto): RIO[ProducerService, Boolean] = ZIO.scoped {
    for {
      kafkaRequest <- ZIO.succeed(ContactPersonCreateRequest(
        businessClientEmail,
        dto.surname,
        dto.name,
        dto.patronymic,
        dto.phoneNumber,
        dto.email
      ))
      metadata <- ZIO.serviceWithZIO[ProducerService](_.produce(kafkaRequest))
      offset <- metadata.map(_.offset()).option
    } yield offset.isDefined
  }

  def updateContactPerson(businessClientEmail: String, dto: ContactPersonUpdateDto): RIO[ProducerService, Boolean] = ZIO.scoped {
    for {
      kafkaRequest <- ZIO.succeed(ContactPersonUpdateRequest(
        businessClientEmail,
        dto.id,
        dto.surname,
        dto.name,
        dto.patronymic,
        dto.phoneNumber,
        dto.email
      ))
      metadata <- ZIO.serviceWithZIO[ProducerService](_.produce(kafkaRequest))
      offset <- metadata.map(_.offset()).option
    } yield offset.isDefined
  }

  def deleteContactPerson(businessClientEmail: String, personId: Long): RIO[ProducerService, Boolean] = ZIO.scoped {
    for {
      kafkaRequest <- ZIO.succeed(ContactPersonDeleteRequest(
        businessClientEmail, personId
      ))
      metadata <- ZIO.serviceWithZIO[ProducerService](_.produce(kafkaRequest))
      offset <- metadata.map(_.offset()).option
    } yield offset.isDefined
  }

  def isContactPersonLinkedToBusinessClient(businessClientEmail: String, personId: Long): RIO[BusinessClientRepository, Boolean] = for {
    id <- businessClientService.getIdByEmail(businessClientEmail)
    res <- contactPersonRepository.isPersonLinkedToClient(id, personId)
  } yield res

}

object ContactPersonService {
  val layer: RLayer[ContactPersonRepository & BusinessClientService, ContactPersonService] =
    ZLayer.fromFunction(ContactPersonService.apply(_, _))
}
