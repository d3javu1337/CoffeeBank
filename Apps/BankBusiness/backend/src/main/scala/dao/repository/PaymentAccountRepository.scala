package dao.repository

import dto.paymentaccount.PaymentAccountReadDto
import model.{BusinessClient, PaymentAccount}
import zio.{Task, URLayer, ZLayer}

import java.util.UUID
import javax.sql.DataSource

trait PaymentAccountRepository {
  def findByClientId(clientId: Long): Task[Option[PaymentAccountReadDto]]
  def isTokenValid(paymentAccountId: Long, token: UUID): Task[Boolean]
  def findInvoiceCreateTokenByClientId(paymentAccountId: Long): Task[Option[UUID]]
  def findIdByClientEmail(businessClientEmail: String): Task[Option[Long]]
}

case class PaymentAccountRepositoryLive(private val ds: DataSource) extends PaymentAccountRepository {

  import io.getquill.*
  import dao.DatabaseContext._

  private inline def paymentAccounts = quote(query[PaymentAccount])
  private inline def businessClients = quote(query[BusinessClient])

  private val dsLayer = ZLayer.succeed(ds)

  override def findByClientId(clientId: Long): Task[Option[PaymentAccountReadDto]] = {
    run(quote{
      paymentAccounts
        .filter(_.businessClientId == lift(clientId))
        .map(acc => PaymentAccountReadDto(acc.id, acc.name, acc.deposit))
    })
      .mapBoth(e => Throwable(e.getMessage), _.headOption)
      .provideLayer(dsLayer)
  }

  override def isTokenValid(paymentAccountId: Long, token: UUID): Task[Boolean] = {
    run(quote(paymentAccounts.filter(acc => acc.id == lift(paymentAccountId) && acc.invoiceCreateToken == lift(token)).size))
      .mapBoth(e => Throwable(e.getMessage), c => c==1)
      .provideLayer(dsLayer)
  }

  override def findInvoiceCreateTokenByClientId(paymentAccountId: Long): Task[Option[UUID]] = {
    run(quote(paymentAccounts.filter(_.id==lift(paymentAccountId)).map(_.invoiceCreateToken)))
      .mapBoth(e => Throwable(e.getMessage), _.headOption)
      .provideLayer(dsLayer)
  }

  override def findIdByClientEmail(businessClientEmail: String): Task[Option[Long]] = {
    run(quote{
      paymentAccounts
        .join(businessClients)
        .on((p, c) => p.businessClientId == c.id)
        .filter((p, c) => c.email == lift(businessClientEmail))
        .map((p, c) => p.id)
    })
      .mapBoth(e => Throwable(e.getMessage), _.headOption)
      .provideLayer(dsLayer)
  }
}

object PaymentAccountRepositoryLive {
  val layer: URLayer[DataSource, PaymentAccountRepositoryLive] = ZLayer.fromFunction(PaymentAccountRepositoryLive.apply _)
}