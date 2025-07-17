package dao.repository

import dto.contactperson.ContactPersonReadDto
import model.ContactPerson
import zio.{Task, URLayer, ZLayer}

import javax.sql.DataSource

trait ContactPersonRepository {
  def findAllByBusinessClientId(businessClientId: Long): Task[List[ContactPersonReadDto]]
  def findById(id: Long): Task[Option[ContactPerson]]
  def findDtoById(businessClientId: Long, id: Long): Task[Option[ContactPersonReadDto]]
  def isPersonLinkedToClient(businessClientId: Long, personId: Long): Task[Boolean]
}

case class ContactPersonRepositoryLive(private val ds: DataSource) extends ContactPersonRepository {

  import io.getquill.*
  import dao.DatabaseContext._

  private val dsLayer = ZLayer.succeed(ds)

  private inline def contactPersons = quote(query[ContactPerson])

  override def findAllByBusinessClientId(businessClientId: Long): Task[List[ContactPersonReadDto]] = {
    run(quote {
      contactPersons
        .filter(_.businessClientId == lift(businessClientId))
        .map(c => ContactPersonReadDto(c.surname, c.name, c.patronymic, c.phoneNumber, c.email))
    })
      .mapError(e => Throwable(e.getMessage))
      .provideLayer(dsLayer)
  }

  override def findById(id: Long): Task[Option[ContactPerson]] = {
    run(quote {
      contactPersons.filter(_.id == lift(id))
    })
      .mapBoth(e => Throwable(e.getMessage), _.headOption)
      .provideLayer(dsLayer)
  }

  override def findDtoById(businessClientId: Long, contactPersonId: Long): Task[Option[ContactPersonReadDto]] = {
    run(quote {
      contactPersons
        .filter(p => p.id == lift(contactPersonId) && p.businessClientId == lift(businessClientId))
        .map(c => ContactPersonReadDto(c.surname, c.name, c.patronymic, c.phoneNumber, c.email))
    })
      .mapBoth(e => Throwable(e.getMessage), _.headOption)
      .provideLayer(dsLayer)
  }

  override def isPersonLinkedToClient(businessClientId: Long, personId: Long): Task[Boolean] = {
    run(quote {
      contactPersons.filter(c => c.id == lift(personId) && c.businessClientId == lift(businessClientId)).size
    })
      .mapBoth(e => Throwable(e.getMessage), _ == 1)
      .provideLayer(dsLayer)
  }
}

object ContactPersonRepositoryLive {
  val layer: URLayer[DataSource, ContactPersonRepositoryLive] = ZLayer.fromFunction(ContactPersonRepositoryLive.apply _)
}