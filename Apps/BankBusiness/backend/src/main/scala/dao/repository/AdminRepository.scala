package dao.repository

import dto.businessclient.BusinessClientCompact
import dto.contactperson.ContactPersonReadDto
import model.{Admin, BusinessClient, ContactPerson}
import zio.{Task, URLayer, ZLayer}

import javax.sql.DataSource

trait AdminRepository {
  def isAdmin(email: String): Task[Boolean]
  def findAllClients: Task[List[BusinessClientCompact]]
  def findClientById(clientId: Long): Task[Option[BusinessClientCompact]]
  def findAllContactsByClientId(clientId: Long): Task[List[ContactPersonReadDto]]
}

case class AdminRepositoryLive(private val ds: DataSource) extends AdminRepository {

  import dao.DatabaseContext.*
  import io.getquill.*

  private val dsLayer = ZLayer.succeed(ds)

  private inline def admins = quote(query[Admin])
  private inline def businessClients = quote(query[BusinessClient])
  private inline def contactPersons = quote(query[ContactPerson])


  override def isAdmin(email: String): Task[Boolean] = {
    run(
      quote{
        admins
          .filter(adm => adm.email == lift(email))
          .nonEmpty
      }).mapError(e => Throwable(e.getMessage))
      .provideLayer(dsLayer)
  }

  override def findAllClients: Task[List[BusinessClientCompact]] = {
    run(
      quote{
        businessClients
          .map(c => BusinessClientCompact(c.id, c.officialName, c.brand, c.email))
      }).mapError(e => Throwable(e.getMessage))
      .provideLayer(dsLayer)
  }

  override def findClientById(clientId: Long): Task[Option[BusinessClientCompact]] = {
    run(
      quote{
        businessClients
          .filter(c => c.id == lift(clientId))
          .map(c => BusinessClientCompact(c.id, c.officialName, c.brand, c.email))
      }).mapBoth(e => Throwable(e.getMessage), _.headOption)
      .provideLayer(dsLayer)
  }


  override def findAllContactsByClientId(clientId: Long): Task[List[ContactPersonReadDto]] = {
    run(
      quote{
        contactPersons
          .filter(cp => cp.businessClientId == lift(clientId))
          .map(cp => ContactPersonReadDto(cp.id, cp.surname, cp.name, cp.patronymic, cp.phoneNumber, cp.email))
      }).mapError(e => Throwable(e.getMessage))
      .provideLayer(dsLayer)
  }
}

object AdminRepositoryLive {
  val layer: URLayer[DataSource, AdminRepositoryLive] = ZLayer.fromFunction(AdminRepositoryLive.apply _)
}