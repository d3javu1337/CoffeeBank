package org.d3javu
package domain.base

import enumeratum.values.{StringEnum, StringEnumEntry}
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

  @newtype
  final case class AccountName(asString: String)

  @newtype
  final case class AccountDeposit(asDouble: Double)

  @newtype
  final case class Cards(asList: List[CardId])

}

sealed abstract class AccountType(val value: String) extends StringEnumEntry

object AccountType extends StringEnum[AccountType] {
  final case object PERSONAL extends AccountType("Личный счёт")
  final case object BUSINESS extends AccountType("Расчетный счет")

  override def values: IndexedSeq[AccountType] = findValues
}