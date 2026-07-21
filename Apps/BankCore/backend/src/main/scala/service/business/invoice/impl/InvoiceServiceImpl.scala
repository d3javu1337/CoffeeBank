package org.d3javu
package service.business.invoice.impl

import java.util.UUID

import org.d3javu.backend.grpc.Transactions.{
  InvoiceIssueRequest, InvoiceIssueResponse, InvoiceIssuingTokenCreateRequest,
  InvoiceIssuingTokenCreateResponse, InvoiceServiceFs2Grpc,
}
import org.d3javu.domain.Errors
import org.d3javu.domain.business.{Invoice, PaymentAccount}
import org.d3javu.repository.business.invoice.InvoiceRepository
import org.d3javu.repository.business.paymentaccount.PaymentAccountRepository

import service.business.invoice.InvoiceService
import cats.syntax.all._
import cats.{Monad, MonadThrow}
import io.grpc.Metadata

class InvoiceServiceImpl[F[_]: MonadThrow](
    invoiceRepository: InvoiceRepository[F],
    paymentAccountRepository: PaymentAccountRepository[F],
) extends InvoiceService[F] {

  override def existsInvoice(id: Invoice.Id): F[Boolean] = {
    invoiceRepository.existsInvoiceById(id)
  }

  override def getInvoiceAmount(id: Invoice.Id): F[Invoice.Amount] = for {
    maybeAmount <- invoiceRepository.getInvoiceAmountById(id)
    amount <- maybeAmount.liftTo[F](Errors.InvoiceNotFound(id))
  } yield amount

  override def getRecipientPaymentAccountId(
      id: Invoice.Id,
  ): F[PaymentAccount.Id] = for {
    maybeId <- invoiceRepository.getInvoiceProviderPaymentAccountIdById(id)
    id <- maybeId.liftTo[F](Errors.InvoiceNotFound(id))
  } yield id

  override def generateInvoicePaymentLink(id: Invoice.Id): String = {
    s"/transaction/invoice?id=${id.asUUID}"
  }

  override def invoiceIssue(
      request: InvoiceIssueRequest,
      ctx: Metadata,
  ): F[InvoiceIssueResponse] = for {
    invoiceId <- invoiceRepository.createInvoice(
      PaymentAccount.Id(request.providerPaymentAccountId),
      Invoice.Amount(request.amount),
    )
    paymentLink = generateInvoicePaymentLink(invoiceId)
  } yield InvoiceIssueResponse(paymentLink = paymentLink)

  override def invoiceIssuingTokenCreate(
      request: InvoiceIssuingTokenCreateRequest,
      ctx: Metadata,
  ): F[InvoiceIssuingTokenCreateResponse] = for {
    token <- UUID.randomUUID().pure[F]
    _ <- paymentAccountRepository.createInvoiceIssuingToken(
      PaymentAccount.Id(request.paymentAccountId),
      PaymentAccount.InvoiceIssueToken(token),
    )
  } yield InvoiceIssuingTokenCreateResponse(token.toString)
}
