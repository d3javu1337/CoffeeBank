package dao.postgres.repository

import zio.{Task, URLayer, ZLayer}

import java.util.UUID
import javax.sql.DataSource

trait InvoiceRepository {
  def isInvoiceExists(invoiceId: UUID): Task[Boolean]
  def findInvoiceAmount(invoiceId: UUID): Task[Option[Double]]
}

case class InvoiceRepositoryLive(private val ds: DataSource) extends InvoiceRepository {

  import io.getquill._
  import dao.postgres.DatabaseContext._

  private val dsLayer = ZLayer.succeed(ds)

  override def isInvoiceExists(invoiceId: UUID): Task[Boolean] = {
    run(
      quote {
        sql"""select count(*)
           from invoice i
           where i.id = ${lift(invoiceId)}
           """.as[Query[Long]]
      })
      .mapBoth(e => Throwable(e.getMessage), _.size == 1)
      .provideLayer(dsLayer)
  }

  override def findInvoiceAmount(invoiceId: UUID): Task[Option[Double]] = {
    run(
      quote{
        sql"""select i.amount
              from invoice i
              where i.id = ${lift(invoiceId)}
           """.as[Query[Double]]
      })
      .mapBoth(e => Throwable(e.getMessage), _.headOption)
      .provideLayer(dsLayer)
  }
}

object InvoiceRepository {
  val layer: URLayer[DataSource, InvoiceRepository] = ZLayer.fromFunction(InvoiceRepositoryLive.apply _)
}