package org.d3javu
package domain.base

import enumeratum.values.{StringEnum, StringEnumEntry}
import io.circe.{Decoder, Encoder}
import io.estatico.newtype.macros.newtype
import org.d3javu.domain.base.Card.CardId
import org.d3javu.domain.base.PersonalAccount.{AccountDeposit, AccountId, AccountName, Cards}

case class PersonalAccount(
                            id: AccountId,
                            name: AccountName,
                            deposit: AccountDeposit,
                            cards: Cards,
                            client: Client.Id,
                            accountType: AccountType
                          )

object PersonalAccount {

  @newtype
  final case class AccountId(asLong: Long)

  object AccountId {
    implicit val decoder: Decoder[AccountId] = deriving
    implicit val encoder: Encoder[AccountId] = deriving
  }

  @newtype
  final case class AccountName(asString: String)

  object AccountName {
    implicit val decoder: Decoder[AccountName] = deriving
    implicit val encoder: Encoder[AccountName] = deriving
  }

  @newtype
  final case class AccountDeposit(asDouble: Double)

  object AccountDeposit {
    implicit val decoder: Decoder[AccountDeposit] = deriving
    implicit val encoder: Encoder[AccountDeposit] = deriving

  }

  @newtype
  final case class Cards(asList: List[CardId])

  object Cards {
    implicit val decoder: Decoder[Cards] = deriving
    implicit val encoder: Encoder[Cards] = deriving

  }

}

sealed abstract class AccountType(val value: String) extends StringEnumEntry

object AccountType extends StringEnum[AccountType] {
  final case object PERSONAL extends AccountType("Личный счёт")
  final case object BUSINESS extends AccountType("Расчетный счет")

  override def values: IndexedSeq[AccountType] = findValues
}