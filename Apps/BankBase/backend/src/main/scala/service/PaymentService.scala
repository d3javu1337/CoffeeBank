package service

import dao.postgres.repository.PaymentRepository
import zio.{RIO, ULayer, ZIO, ZLayer}

import java.util.UUID

case class PaymentService() {

  def isInvoicePayed(invoiceId: String): RIO[PaymentRepository, Boolean] = {
    ZIO.serviceWithZIO[PaymentRepository](_.isInvoicePayed(UUID.fromString(invoiceId)))
  }

}

object PaymentService {
  val layer: ULayer[PaymentService] = ZLayer.derive[PaymentService]
}