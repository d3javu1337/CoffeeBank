package org.d3javu
package service.business.invoice

import domain.business.{Invoice, PaymentAccount}

import io.grpc.Metadata
import org.d3javu.backend.grpc.Transactions.InvoiceServiceFs2Grpc

trait InvoiceService[F[_]] extends InvoiceServiceFs2Grpc[F, Metadata] {
  def existsInvoice(id: Invoice.Id): F[Boolean]
  def getInvoiceAmount(id: Invoice.Id): F[Invoice.Amount]
  def getRecipientPaymentAccountId(id: Invoice.Id): F[PaymentAccount.Id]
  def generateInvoicePaymentLink(id: Invoice.Id): String
}
