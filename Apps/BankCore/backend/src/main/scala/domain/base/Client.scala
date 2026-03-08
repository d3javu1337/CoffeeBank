package org.d3javu
package domain.base

import io.estatico.newtype.macros.newtype
import Client._
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

  @newtype
  final case class Surname(asString: String)

  @newtype
  final case class Name(asString: String)

  @newtype
  final case class Patronymic(asString: String)

  @newtype
  final case class DateOfBirth(asLocalDate: LocalDate)

  @newtype
  final case class PhoneNumber(asString: String)

  @newtype
  final case class DocumentsId(asLong: Long)

  @newtype
  final case class Email(asString: String)

  @newtype
  final case class PasswordHash(asString: String)

  @newtype
  final case class IsEnabled(asBoolean: Boolean)

//  implicit val codec: Codec[Client] = deriveCodec

}