package service

import dao.repository.{BusinessClientRepository, PaymentAccountRepository}
import dto.invoice.InvoiceIssueDto
import errors.NoEntityPresented
import org.d3javu.backend.grpc.invoice.{InvoiceIssueRequest, InvoiceIssuingTokenCreateRequest}
import org.d3javu.backend.grpc.invoice.ZioInvoice.InvoiceServiceClient
import zio.ZIO
import zio.{RIO, RLayer, ZLayer}

import java.util.UUID

case class InvoiceServiceImpl(private val paymentAccountService: PaymentAccountService) {

  def generateApiToken(email: String): RIO[InvoiceServiceClient, String] =
    for {
      id <- paymentAccountService.getAccountIdByClientEmail(email)
      t <- ZIO.succeed(InvoiceIssuingTokenCreateRequest(id))
      r <- InvoiceServiceClient.invoiceIssuingTokenCreate(t)
        .tapError(e => ZIO.logError(e.getMessage))
    } yield r.token

  def invoiceIssue(email: String, invoiceIssueDto: InvoiceIssueDto): RIO[InvoiceServiceClient, String] = for {
    id <- paymentAccountService.getAccountIdByClientEmail(email)
    r <- ZIO.succeed(InvoiceIssueRequest(id, invoiceIssueDto.amount))
    t <- InvoiceServiceClient.invoiceIssue(r)
  } yield t.paymentLink

  def getInvoiceCreateToken(businessClientEmail: String): RIO[PaymentAccountRepository & BusinessClientRepository, UUID] = {
    paymentAccountService.getInvoiceCreateTokenByClientEmail(businessClientEmail)
      .someOrFail(NoEntityPresented())
  }
}

object InvoiceServiceImpl {
  val layer: RLayer[PaymentAccountService, InvoiceServiceImpl] = ZLayer.fromFunction(InvoiceServiceImpl.apply _)
}