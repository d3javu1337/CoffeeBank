package org.d3javu
package repository.business.paymentaccount

import domain.business.{Client, PaymentAccount}

trait PaymentAccountRepository[F[_]] {
  def createPaymentAccount(businessClientId: Client.Id): F[PaymentAccount.Id]
  def existsPaymentAccountByClientId(clientId: Client.Id): F[Boolean]
  def createInvoiceIssuingToken(
      paymentAccountId: PaymentAccount.Id,
      token: PaymentAccount.InvoiceIssueToken,
  ): F[Unit]
}
