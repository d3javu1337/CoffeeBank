package dao.postgres.repository

import zio.{Task, URLayer, ZLayer}

import java.util.UUID
import javax.sql.DataSource

trait PaymentRepository {
  def isInvoicePayed(invoiceId: UUID): Task[Boolean]
}

case class PaymentRepositoryLive(private val ds: DataSource) extends PaymentRepository {

  import io.getquill._
  import dao.postgres.DatabaseContext._

  private val dsLayer = ZLayer.succeed(ds)

  override def isInvoicePayed(invoiceId: UUID): Task[Boolean] = {
    run(
      quote{
      sql"""select count(*)
           from payment p
           where p.invoice_id = ${lift(invoiceId)}
           """.as[Query[Long]]
    })
      .mapBoth(e => Throwable(e.getMessage), c => c.head==1)
      .provideLayer(dsLayer)
  }
}

object PaymentRepository {
  val layer: URLayer[DataSource, PaymentRepositoryLive] = ZLayer.fromFunction(PaymentRepositoryLive.apply _)
}