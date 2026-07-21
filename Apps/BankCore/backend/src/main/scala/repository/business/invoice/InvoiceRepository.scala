package org.d3javu
package repository.business.invoice

import org.d3javu.domain.business.{Invoice, PaymentAccount}

import java.util.UUID

trait InvoiceRepository[F[_]] {
  def existsInvoiceById(id: Invoice.Id): F[Boolean]
  def getInvoiceAmountById(id: Invoice.Id): F[Option[Invoice.Amount]]
  def getInvoiceProviderPaymentAccountIdById(id: Invoice.Id): F[Option[PaymentAccount.Id]]
  def createInvoice(providerPaymentAccountId: PaymentAccount.Id, amount: Invoice.Amount): F[Invoice.Id]
}
