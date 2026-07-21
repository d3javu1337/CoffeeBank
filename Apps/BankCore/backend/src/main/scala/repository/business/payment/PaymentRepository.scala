package org.d3javu
package repository.business.payment

import org.d3javu.domain.base.PersonalAccount
import org.d3javu.domain.transaction.Transaction

import domain.business.{Invoice, Payment, PaymentAccount}

trait PaymentRepository[F[_]] {
  def takeMoneyFromPayer(
      payerAccountId: PersonalAccount.AccountId,
      amount: Invoice.Amount,
  ): F[Int]
  def sendMoneyToRecipient(
      recipientAccountId: PaymentAccount.Id,
      amount: Invoice.Amount,
  ): F[Int]
  def createPayment(
      providerPaymentAccountId: PaymentAccount.Id,
      payerPersonalAccountId: PersonalAccount.AccountId,
      transactionId: Transaction.Id,
      invoiceId: Invoice.Id,
  ): F[Payment.Id]
}
