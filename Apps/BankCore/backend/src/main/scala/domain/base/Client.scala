package org.d3javu
package domain.base

import io.estatico.newtype.macros.newtype
import Client._
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.deriveDecoder
import org.d3javu.domain.base.PersonalAccount.AccountId
import skunk.Codec

import java.time.LocalDate

case class Client(
                 id: Id,
                 surname: Surname,
                 name: Name,
                 patronymic: Option[Patronymic],
                 dateOfBirth: DateOfBirth,
                 phoneNumber: PhoneNumber,
                 documentsId: DocumentsId,
                 email: Email,
                 passwordHash: PasswordHash,
                 personalAccountId: AccountId,
                 isEnabled: IsEnabled
                 )

object Client {

  @newtype
  final case class Id(asLong: Long)

  object Id {
    implicit val decoder: Decoder[Id] = deriving
    implicit val encoder: Encoder[Id] = deriving
  }

  @newtype
  final case class Surname(asString: String)

  object Surname {
    implicit val decoder: Decoder[Surname] = deriving
    implicit val encoder: Encoder[Surname] = deriving

  }

  @newtype
  final case class Name(asString: String)

  object Name {
    implicit val decoder: Decoder[Name] = deriving
    implicit val encoder: Encoder[Name] = deriving

  }

  @newtype
  final case class Patronymic(asString: String)

  object Patronymic {
    implicit val decoder: Decoder[Patronymic] = deriving
    implicit val encoder: Encoder[Patronymic] = deriving

  }

  @newtype
  final case class DateOfBirth(asLocalDate: LocalDate)

  object DateOfBirth {
    implicit val decoder: Decoder[DateOfBirth] = deriving
    implicit val encoder: Encoder[DateOfBirth] = deriving

  }

  @newtype
  final case class PhoneNumber(asString: String)

  object PhoneNumber {
    implicit val decoder: Decoder[PhoneNumber] = deriving
    implicit val encoder: Encoder[PhoneNumber] = deriving

  }

  @newtype
  final case class DocumentsId(asLong: Long)

  object DocumentsId {
    implicit val decoder: Decoder[DocumentsId] = deriving
    implicit val encoder: Encoder[DocumentsId] = deriving

  }

  @newtype
  final case class Email(asString: String)

  object Email {
    implicit val emailDecoder: Decoder[Email] = deriving
    implicit val emailEncoder: Encoder[Email] = deriving
  }

  @newtype
  final case class PasswordHash(asString: String)

  object PasswordHash {
    implicit val decoder: Decoder[PasswordHash] = deriving
    implicit val encoder: Encoder[PasswordHash] = deriving

  }

  @newtype
  final case class IsEnabled(asBoolean: Boolean)

  object IsEnabled {
    implicit val decoder: Decoder[IsEnabled] = deriving
    implicit val encoder: Encoder[IsEnabled] = deriving

  }

}