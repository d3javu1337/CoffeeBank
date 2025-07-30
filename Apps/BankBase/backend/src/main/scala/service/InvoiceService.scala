package service

import dao.postgres.repository.{InvoiceRepository, PaymentRepository}
import dto.invoice.InvoiceDto
import errors.NoEntityPresented
import zio.{RIO, ULayer, ZIO, ZLayer}

import java.util.UUID

case class InvoiceService() {
  private def isInvoiceExists(invoiceId: String): RIO[InvoiceRepository, Boolean] = {
    ZIO.serviceWithZIO[InvoiceRepository](_.isInvoiceExists(UUID.fromString(invoiceId)))
  }

  private def getInvoiceAmount(invoiceId: String): RIO[InvoiceRepository, Double] = {
    ZIO.serviceWithZIO[InvoiceRepository](_.findInvoiceAmount(UUID.fromString(invoiceId)))
      .someOrFail(NoEntityPresented())
  }

  def getInvoiceInfo(invoiceId: String): RIO[InvoiceRepository & PaymentRepository & PaymentService, InvoiceDto] =
    for {
      isExists <- isInvoiceExists(invoiceId)
        .flatMap(res => ZIO.fail(Throwable("invoice does not exists")).unless(res))
      isPayed <- ZIO.serviceWithZIO[PaymentService](_.isInvoicePayed(invoiceId))
        .flatMap(res => ZIO.fail(Throwable("already payed")).when(res))
      dto <- getInvoiceAmount(invoiceId).map(InvoiceDto(_))
    } yield dto

}

object InvoiceService {
  val layer: ULayer[InvoiceService] = ZLayer.derive[InvoiceService]
}