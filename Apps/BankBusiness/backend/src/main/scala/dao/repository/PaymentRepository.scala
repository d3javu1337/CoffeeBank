package dao.repository

import dto.payment.PaymentWithAmountDto
import model.{Invoice, Payment}
import zio.{Task, URLayer, ZLayer}

import java.util.UUID
import javax.sql.DataSource

trait PaymentRepository {
  def findDtoById(id: UUID): Task[Option[PaymentWithAmountDto]]
  def findPaymentAccountIdById(id: UUID): Task[Option[Long]]
  def findAllByPaymentAccountId(paymentAccountId: Long): Task[List[PaymentWithAmountDto]]
  def checkPaymentByInvoiceId(invoiceId: UUID): Task[Boolean]
}

case class PaymentRepositoryLive(private val ds: DataSource) extends PaymentRepository {

  import io.getquill.*
  import dao.DatabaseContext._

  private inline def payments = quote(query[Payment])
  private inline def invoices = quote(query[Invoice])

  private val dsLayer = ZLayer.succeed(ds)

  override def findDtoById(paymentId: UUID): Task[Option[PaymentWithAmountDto]] = {
    run(quote{
      payments
        .join(invoices)
        .on({case (p, i) => i.id == p.invoiceId})
        .filter((p, i) => p.id == lift(paymentId))
        .map((p, i) => (p.id, i.amount))
    })
      .mapBoth(e => Throwable(e.getMessage), _.headOption)
      .map(opt => {
        if opt.isDefined then Option(PaymentWithAmountDto(opt.get._1, opt.get._2))
        else Option.empty[PaymentWithAmountDto]
      })
      .provideLayer(dsLayer)
  }

  override def findPaymentAccountIdById(id: UUID): Task[Option[Long]] = {
    run(quote{
      payments
        .filter(_.id == lift(id))
        .map(_.paymentAccountId)
    })
      .mapBoth(e => Throwable(e.getMessage), _.headOption)
      .provideLayer(dsLayer)
  }

  override def findAllByPaymentAccountId(paymentAccountId: Long): Task[List[PaymentWithAmountDto]] = {
    run(quote {
      payments
        .join(invoices)
        .on((p, i) => p.invoiceId == i.id)
        .filter((p, i) => p.paymentAccountId == lift(paymentAccountId))
        .map((p, i) => (p.id, i.amount))
    }).mapBoth(e => Throwable(e.getMessage), c => c.map({case (id, amount) => PaymentWithAmountDto(id, amount)}))
      .provideLayer(dsLayer)
  }

  override def checkPaymentByInvoiceId(invoiceId: UUID): Task[Boolean] = {
    run(quote{
      payments
        .filter(_.invoiceId == lift(invoiceId))
        .size
    })
      .mapBoth(e => Throwable(e.getMessage), _ == 1)
      .provideLayer(dsLayer)
  }
}

object PaymentRepositoryLive {
  val layer: URLayer[DataSource, PaymentRepositoryLive] = ZLayer.fromFunction(PaymentRepositoryLive.apply _)
}