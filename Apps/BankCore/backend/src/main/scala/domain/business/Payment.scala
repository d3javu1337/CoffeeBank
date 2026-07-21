package org.d3javu
package domain.business

import java.util.UUID

import org.d3javu.domain.base.PersonalAccount
import org.d3javu.domain.business.Payment.Id
import org.d3javu.domain.transaction.Transaction

import io.estatico.newtype.macros.newtype

case class Payment(
    id: Id,
    paymentAccountId: PaymentAccount.Id,
    personalAccountId: PersonalAccount.AccountId,
    transactionId: Transaction.Id,
    invoiceId: Invoice.Id,
)

object Payment {

  @newtype
  final case class Id(asUUID: UUID)

}
