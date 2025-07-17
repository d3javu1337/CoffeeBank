package service

import dao.repository.{BusinessClientRepository, PaymentRepository}
import dto.payment.PaymentWithAmountDto
import zio.{RIO, RLayer, Task, ZLayer}

import java.util.UUID

case class PaymentService(
                           private val paymentRepository: PaymentRepository,
                           private val paymentAccountService: PaymentAccountService) {
  def getPaymentByClientEmailAndId(clientEmail: String, paymentId: UUID): RIO[BusinessClientRepository, PaymentWithAmountDto] = for {
    payment <- paymentRepository.findDtoById(paymentId)
      .map(_.get)
    paymentAccountId <- paymentRepository.findPaymentAccountIdById(paymentId)
      .map(_.get)
    paymentAccount <- paymentAccountService.getPaymentAccountByClientEmail(clientEmail)
  } yield payment

  def getAllPaymentsByBusinessClientEmail(businessClientEmail: String): Task[List[PaymentWithAmountDto]] = for {
    accountId <- paymentAccountService.getAccountIdByClientEmail(businessClientEmail)
    payments <- paymentRepository.findAllByPaymentAccountId(accountId)
  } yield payments

  def checkPayment(invoiceId: UUID): Task[Boolean] =
    paymentRepository.checkPaymentByInvoiceId(invoiceId)
}

object PaymentService {
  val layer: RLayer[PaymentRepository & PaymentAccountService, PaymentService] =
    ZLayer.fromFunction(PaymentService.apply(_, _))
}
