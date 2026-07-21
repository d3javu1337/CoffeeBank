package org.d3javu
package domain.business

import io.circe.{Decoder, Encoder}
import io.estatico.newtype.macros.newtype
import org.d3javu.domain.business.ContactPerson._

object ContactPerson {

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
  final case class PhoneNumber(asString: String)

  object PhoneNumber {
    implicit val decoder: Decoder[PhoneNumber] = deriving
    implicit val encoder: Encoder[PhoneNumber] = deriving
  }

  @newtype
  final case class Email(asString: String)

  object Email {
    implicit val decoder: Decoder[Email] = deriving
    implicit val encoder: Encoder[Email] = deriving
  }

}


case class ContactPerson(
                        id: Id,
                        businessClient: Client.Id,
                        surname: Surname,
                        name: Name,
                        patronymic: Patronymic,
                        phoneNumber: PhoneNumber,
                        email: Email
                        )
