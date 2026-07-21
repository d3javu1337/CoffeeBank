package org.d3javu
package domain.business

import io.estatico.newtype.macros.newtype
import PaymentAccount._

import java.util.UUID

case class PaymentAccount(
                         id: Id,
                         name: Name,
                         deposit: Deposit,
                         businessClient: Client.Id
                         )

object PaymentAccount {

  @newtype
  final case class Id(asLong: Long)

  @newtype
  final case class Name(asString: String)

  @newtype
  final case class Deposit(asDouble: Double)

  @newtype
  final case class InvoiceIssueToken(asUuid: UUID)

}
