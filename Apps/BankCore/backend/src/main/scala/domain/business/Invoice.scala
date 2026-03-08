package org.d3javu
package domain.business

import io.estatico.newtype.macros.newtype
import org.d3javu.domain.business.Invoice._

import java.util.UUID

case class Invoice(
                  id: Id,
                  amount: Amount,
                  providerPaymentAccount: PaymentAccount.Id
                  )

object Invoice {

  @newtype
  final case class Id(asUUID: UUID)

  @newtype
  final case class Amount(asDouble: Double)

}
