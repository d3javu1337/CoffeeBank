package service

import dao.repository.{BusinessClientRepository, PaymentAccountRepository}
import dto.paymentaccount.PaymentAccountReadDto
import errors.NoEntityPresented
import kafka.ProducerService
import kafka.messages.paymentaccount.PaymentAccountCreateRequest
import zio.{RIO, RLayer, Task, ZIO, ZLayer}

import java.util.UUID

case class PaymentAccountService(
                                  private val paymentAccountRepository: PaymentAccountRepository,
                                  private val businessClientService: BusinessClientService) {
  def getPaymentAccountByClientEmail(clientEmail: String): RIO[BusinessClientRepository, PaymentAccountReadDto] = (for {
    clientId <- businessClientService.getIdByEmail(clientEmail)
    account <- paymentAccountRepository.findByClientId(clientId)
  } yield account)
      .someOrFail(NoEntityPresented())

  def getInvoiceCreateTokenByClientEmail(clientEmail: String): RIO[BusinessClientRepository, Option[UUID]] = for {
    clientId <- businessClientService.getIdByEmail(clientEmail)
    token <- paymentAccountRepository.findInvoiceCreateTokenByClientId(clientId)
  } yield token

  def createPaymentAccount(clientEmail: String): RIO[ProducerService & BusinessClientRepository, Boolean] = ZIO.scoped{
    for {
      id <- businessClientService.getIdByEmail(clientEmail)
      kafkaRequest <- ZIO.succeed(PaymentAccountCreateRequest(id, clientEmail))
      metadata <- ZIO.serviceWithZIO[ProducerService](_.produce(kafkaRequest))
      offset <- metadata.map(_.offset()).option
    } yield offset.isDefined
  }

  def getAccountIdByInvoiceToken(token: UUID): RIO[BusinessClientRepository, Long] = 
    paymentAccountRepository.findIdByToken(token)
      .someOrFail(NoEntityPresented())

  def getAccountIdByClientEmail(businessClientEmail: String): Task[Long] =
    paymentAccountRepository.findIdByClientEmail(businessClientEmail)
    .someOrFail(NoEntityPresented())

}

object PaymentAccountService {
  val layer: RLayer[PaymentAccountRepository & BusinessClientService, PaymentAccountService] =
    ZLayer.fromFunction(PaymentAccountService.apply(_,_))
}