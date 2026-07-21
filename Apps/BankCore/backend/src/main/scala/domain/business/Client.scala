package org.d3javu
package domain.business

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import io.estatico.newtype.macros.newtype
import org.d3javu.domain.business.Client._

case class Client(
                 id: Id,
                 officialName: OfficialName,
                 brand: Brand,
                 email: Email,
                 passwordHash: PasswordHash
                 )

object Client {

  @newtype
  final case class Id(asLong: Long)

  object Id {
    implicit val decoder: Decoder[Id] = deriving
    implicit val encoder: Encoder[Id] = deriving
  }

  @newtype
  final case class OfficialName(asString: String)

  object OfficialName {
    implicit val decoder: Decoder[OfficialName] = deriving
    implicit val encoder: Encoder[OfficialName] = deriving

  }

  @newtype
  final case class Brand(asString: String)

  object Brand {
    implicit val decoder: Decoder[Brand] = deriving
    implicit val encoder: Encoder[Brand] = deriving

  }

  @newtype
  final case class Email(asString: String)

  object Email {
    implicit val decoder: Decoder[Email] = deriving
    implicit val encoder: Encoder[Email] = deriving

  }

  @newtype
  final case class PasswordHash(asString: String)

  object PasswordHash {
    implicit val decoder: Decoder[PasswordHash] = deriving
    implicit val encoder: Encoder[PasswordHash] = deriving
  }

}