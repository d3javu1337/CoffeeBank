package dao.repository

import model.{Invoice, Payment, PaymentAccount}
import zio.{URLayer, Task, ZLayer}

import javax.sql.DataSource

trait InvoiceRepository {
  def findAllInvoicesByPaymentAccountId(paymentAccountId: Long): Task[List[Invoice]]
}

case class InvoiceRepositoryLive(private val ds: DataSource) extends InvoiceRepository {

  import io.getquill.*
  import dao.DatabaseContext._


  private inline def paymentAccounts = quote(query[PaymentAccount])
  private inline def invoices = quote(query[Invoice])

  private val dsLayer = ZLayer.succeed(ds)

  override def findAllInvoicesByPaymentAccountId(paymentAccountId: Long): Task[List[Invoice]] = {
    run(
      quote{
        invoices
          .join(paymentAccounts)
          .on((i, p) => i.providerPaymentAccountId==p.id)
          .filter((i, p) => p.id==lift(paymentAccountId))
          .map((i, p) => i)
      }
    ).mapError(e => Throwable(e.getMessage))
      .provideLayer(dsLayer)
  }
}

object InvoiceRepositoryLive {
  val layer: URLayer[DataSource, InvoiceRepositoryLive] = ZLayer.fromFunction(InvoiceRepositoryLive.apply _)
}