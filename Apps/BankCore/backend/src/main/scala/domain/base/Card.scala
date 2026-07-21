package org.d3javu
package domain.base

import enumeratum.EnumEntry
import enumeratum.values.{StringEnum, StringEnumEntry}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import io.estatico.newtype.macros.newtype
import org.d3javu.domain.base.Card.{CardId, CardName, CardNumber, ExpirationDate, PinHash, SecurityCode}
import org.d3javu.domain.base.PersonalAccount.AccountId

import java.time.LocalDate

object Card {

  @newtype
  final case class CardId(asLong: Long)

  object CardId {
    implicit val decoder: Decoder[CardId] = deriving
    implicit val encoder: Encoder[CardId] = deriving
  }

  @newtype
  final case class CardName(asString: String)

  object CardName {
    implicit val decoder: Decoder[CardName] = deriving
    implicit val encoder: Encoder[CardName] = deriving

  }

  @newtype
  final case class CardNumber(asString: String)

  object CardNumber {
    implicit val decoder: Decoder[CardNumber] = deriving
    implicit val encoder: Encoder[CardNumber] = deriving
  }

  @newtype
  final case class ExpirationDate(asDate: LocalDate)

  object ExpirationDate {
    implicit val decoder: Decoder[ExpirationDate] = deriving
    implicit val encoder: Encoder[ExpirationDate] = deriving

  }

  @newtype
  final case class PinHash(asString: String)

  object PinHash {
    implicit val decoder: Decoder[PinHash] = deriving
    implicit val encoder: Encoder[PinHash] = deriving

  }

  @newtype
  final case class SecurityCode(asString: String)

  object SecurityCode {
    implicit val decoder: Decoder[SecurityCode] = deriving
    implicit val encoder: Encoder[SecurityCode] = deriving

  }

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

  implicit val decoder: Decoder[CardType] = deriveDecoder[CardType]
  implicit val encoder: Encoder[CardType] = deriveEncoder[CardType]

}
