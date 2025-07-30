package dao.postgres.repository

import dto.auth.AuthData
import dto.client.ClientReadDto
import model.client.Client
import security.Principal
import zio.{Task, URLayer, ZLayer}

import javax.sql.DataSource

trait ClientRepository {
  def findDtoById(clientId: Long): Task[Option[ClientReadDto]]

  def findDtoByEmail(email: String): Task[Option[ClientReadDto]]

  def findIdByEmail(email: String): Task[Option[Long]]

  def existsClientByEmail(email: String): Task[Boolean]

  def verifyCredentials(email: String, passwordHash: String): Task[Boolean]

  def loadPrincipal(email: String): Task[Option[Principal]]

  def loadAuthData(email: String): Task[Option[AuthData]]
}

final case class ClientRepositoryLive(private val ds: DataSource) extends ClientRepository {

  import io.getquill._
  import dao.postgres.DatabaseContext._

  private val dsLayer = ZLayer.succeed(ds)

  private inline def clients = quote(query[Client])

  override def findDtoById(clientId: Long): Task[Option[ClientReadDto]] = {
    run(quote {
      clients
        .filter(_.id == lift(clientId))
        .map(c => ClientReadDto(c.id, c.surname, c.name, c.patronymic))
    })
      .mapBoth(e => Throwable(e.getMessage), _.headOption)
      .provideLayer(dsLayer)
  }

  override def findDtoByEmail(email: String): Task[Option[ClientReadDto]] = {
    run(quote {
      clients
        .filter(_.email == lift(email))
        .map(c => ClientReadDto(c.id, c.surname, c.name, c.patronymic))
    })
      .mapBoth(e => Throwable(e.getMessage), _.headOption)
      .provideLayer(dsLayer)
  }

  override def findIdByEmail(email: String): Task[Option[Long]] = {
    run(quote {
      clients
        .filter(_.email == lift(email))
        .map(_.id)
    })
      .mapBoth(e => Throwable(), _.headOption)
      .provideLayer(dsLayer)
  }

  override def existsClientByEmail(email: String): Task[Boolean] = {
    run(quote {
      clients
        .filter(_.email == lift(email))
        .size
    })
      .mapBoth(e => Throwable(e.getMessage), _ == 1)
      .provideLayer(dsLayer)
  }

  override def verifyCredentials(email: String, passwordHash: String): Task[Boolean] = {
    run(quote {
      clients
        .filter(c => c.email == lift(email) && c.passwordHash == lift(passwordHash) && c.isEnabled)
        .size
    })
      .mapBoth(e => Throwable(e.getMessage), _ == 1)
      .provideLayer(dsLayer)
  }

  override def loadPrincipal(email: String): Task[Option[Principal]] = {
    run(quote {
      clients
        .filter(c => c.email == lift(email) && c.isEnabled)
        .map(c => Principal(c.email))
    })
      .mapBoth(e => Throwable(), _.headOption)
      .provideLayer(dsLayer)
  }

  override def loadAuthData(email: String): Task[Option[AuthData]] = {
    run(quote {
      clients
        .filter(c => c.email == lift(email) && c.isEnabled)
        .map(c => AuthData(c.email, c.passwordHash))
    })
      .mapBoth(e => Throwable(e.getMessage), _.headOption)
      .provideLayer(dsLayer)
  }
}

object ClientRepository {
  val layer: URLayer[DataSource, ClientRepository] = ZLayer.fromFunction(ClientRepositoryLive.apply _)
}