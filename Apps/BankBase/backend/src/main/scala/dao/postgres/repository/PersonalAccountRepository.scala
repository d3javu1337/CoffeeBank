package dao.postgres.repository

import dto.personalaccount.PersonalAccountReadDto
import model.client.Client
import model.personalaccount.PersonalAccount
import zio.{Task, URLayer, ZLayer}

import javax.sql.DataSource

trait PersonalAccountRepository {
  def findAccountByClientId(clientId: Long): Task[List[PersonalAccountReadDto]]
  def isClientOwnsAccount(clientId: Long, accountId: Long): Task[Boolean]
  def findAccountIdByClientId(clientId: Long): Task[Option[Long]]
  def findAccountByClientEmail(clientEmail: String): Task[Option[PersonalAccountReadDto]]
  def findAccountIdByClientEmail(clientEmail: String): Task[Option[Long]]
}

final case class PersonalAccountRepositoryLive(private val ds: DataSource) extends PersonalAccountRepository {

  import io.getquill._
  import dao.postgres.DatabaseContext.given
  import dao.postgres.DatabaseContext._
  import PersonalAccount.given

  private inline def personalAccounts = quote(query[PersonalAccount])

  private inline def clients = quote(query[Client])

  private val dsLayer = ZLayer.succeed(ds)

  override def findAccountByClientId(clientId: Long): Task[List[PersonalAccountReadDto]] = {
    run(quote{
      personalAccounts
        .filter(_.clientId == lift(clientId))
        .map(a=> PersonalAccountReadDto(a.id, a.name, a.deposit, a.accountType))
    })
      .mapError(e => Throwable(e.getMessage))
      .provideLayer(dsLayer)
  }

  override def isClientOwnsAccount(clientId: Long, accountId: Long): Task[Boolean] = {
    run(quote{
      personalAccounts
        .filter(a => a.id == lift(accountId) && a.clientId == lift(clientId))
        .size
    })
      .mapBoth(e => Throwable(e.getMessage), _==1)
      .provideLayer(dsLayer)
  }

  override def findAccountIdByClientId(clientId: Long): Task[Option[Long]] = {
    run(quote{
      personalAccounts
        .filter(_.clientId == lift(clientId))
        .map(_.id)
    })
      .mapBoth(e => Throwable(e.getMessage), _.headOption)
      .provideLayer(dsLayer)
  }

  override def findAccountByClientEmail(clientEmail: String): Task[Option[PersonalAccountReadDto]] = {
    run(quote{
      personalAccounts
        .join(clients)
        .on((a, c) => a.clientId == c.id)
        .filter((p, c) => c.email == lift(clientEmail))
        .map((a,c) => PersonalAccountReadDto(a.id, a.name, a.deposit, a.accountType))
    })
      .mapBoth(e => Throwable(e.getMessage), _.headOption)
      .provideLayer(dsLayer)
  }

  override def findAccountIdByClientEmail(clientEmail: String): Task[Option[Long]] = {
    run(quote{
      personalAccounts
        .join(clients)
        .on((p, c) => p.clientId == c.id)
        .filter((p, c) => c.email == lift(clientEmail))
        .map((p, c) => p.id)
    })
      .mapBoth(e => Throwable(e.getMessage), _.headOption)
      .provideLayer(dsLayer)
  }
}

object PersonalAccountRepository {
  val layer: URLayer[DataSource, PersonalAccountRepository] = ZLayer.fromFunction(PersonalAccountRepositoryLive.apply _)
}