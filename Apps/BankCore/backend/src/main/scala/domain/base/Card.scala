package org.d3javu
package domain.base

import enumeratum.EnumEntry
import enumeratum.values.{StringEnum, StringEnumEntry}
import io.estatico.newtype.macros.newtype
import org.d3javu.domain.base.Card.{CardId, CardName, CardNumber, ExpirationDate, PinHash, SecurityCode}
import org.d3javu.domain.base.PersonalAccount.AccountId

import java.util.Date

object Card {

  @newtype
  final case class CardId(asLong: Long)

  @newtype
  final case class CardName(asString: String)

  @newtype
  final case class CardNumber(asString: String)

  @newtype
  final case class ExpirationDate(asDate: Date)

  @newtype
  final case class PinHash(asString: String)

  @newtype
  final case class SecurityCode(asString: String)

}

case class Card(
                 id: CardId,
                 name: CardName,
                 cardType: CardType,
                 number: CardNumber,
                 expirationDate: ExpirationDate,
                 personalAccountId: AccountId,
                 pinHash: Option[PinHash],
                 securityCode: SecurityCode
               )




sealed abstract class CardType(val value: String) extends StringEnumEntry

object CardType extends StringEnum[CardType] {
  final case object CREDIT extends CardType("Кредитная карта")
  final case object DEBIT extends CardType("Дебетовая карта")
  final case object OVERDRAFT extends CardType("Карта с овердрафтом")
  final case object PREPAID extends CardType("Предоплаченная карта")

  override def values: IndexedSeq[CardType] = findValues
}