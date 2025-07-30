package service

import dao.postgres.repository.{ClientRepository, PersonalAccountRepository}
import dto.personalaccount.PersonalAccountReadDto
import errors.NoEntityPresented
import internalkafka.ProducerService
import internalkafka.messages.personalaccount.{PersonalAccountCreateRequest, PersonalAccountRenameRequest}
import service.PersonalAccountService.{OpenPersonalAccountEnv, RenamePersonalAccountEnv}
import web.requests.personalaccount.PersonalAccountRename
import zio.{RIO, Task, TaskLayer, URLayer, ZIO, ZLayer}

case class PersonalAccountService() {
  def getAccountByClientId(clientId: Long): RIO[PersonalAccountRepository, PersonalAccountReadDto] = {
    ZIO.serviceWithZIO[PersonalAccountRepository](_.findAccountByClientId(clientId))
      .map(_.headOption)
      .someOrFail(NoEntityPresented())
  }

  def isClientOwnsAccount(clientId: Long, accountId: Long): RIO[PersonalAccountRepository, Boolean] = {
    ZIO.serviceWithZIO[PersonalAccountRepository](_.isClientOwnsAccount(clientId, accountId))
  }

  def getAccountIdByClientId(clientId: Long): RIO[PersonalAccountRepository, Long] = {
    ZIO.serviceWithZIO[PersonalAccountRepository](_.findAccountIdByClientId(clientId))
      .someOrFail(NoEntityPresented())
  }

  def getAccountByClientEmail(clientEmail: String): RIO[PersonalAccountRepository, PersonalAccountReadDto] = {
    ZIO.serviceWithZIO[PersonalAccountRepository](_.findAccountByClientEmail(clientEmail))
      .someOrFail(NoEntityPresented())
  }

  def getAccountIdByClientEmail(clientEmail: String): RIO[PersonalAccountRepository, Long] = {
    ZIO.serviceWithZIO[PersonalAccountRepository](_.findAccountIdByClientEmail(clientEmail))
      .someOrFail(NoEntityPresented())
  }

  def openPersonalAccount(clientEmail: String): RIO[OpenPersonalAccountEnv, Boolean] = ZIO.scoped {
    for {
      clientId <- ZIO.serviceWithZIO[ClientService](_.getIdByEmail(clientEmail))
      kafkaRequest <- ZIO.succeed(PersonalAccountCreateRequest(
        clientId,
        clientEmail
      ))
      metadata <- ZIO.serviceWithZIO[ProducerService](_.produce(kafkaRequest))
      offset <- metadata.map(m => Option(m.offset()))
    } yield offset.isDefined
  }

  def renamePersonalAccount(clientEmail: String, renameRequest: PersonalAccountRename): RIO[RenamePersonalAccountEnv, Boolean] = ZIO.scoped {
    for {
      clientId <- ZIO.serviceWithZIO[ClientService](_.getIdByEmail(clientEmail))
      kafkaRequest <- ZIO.succeed(PersonalAccountRenameRequest(
        clientId,
        clientEmail,
        renameRequest.accountId,
        renameRequest.newName
      ))
      metadata <- ZIO.serviceWithZIO[ProducerService](_.produce(kafkaRequest))
      offset <- metadata.map(m => Option(m.offset()))
    } yield offset.isDefined
  }

}

object PersonalAccountService {

  private type OpenPersonalAccountEnv = ProducerService &
    ClientService &
    ClientRepository

  private type RenamePersonalAccountEnv = ProducerService &
    ClientService &
    ClientRepository

  val layer: TaskLayer[PersonalAccountService] = ZLayer.derive[PersonalAccountService]
}