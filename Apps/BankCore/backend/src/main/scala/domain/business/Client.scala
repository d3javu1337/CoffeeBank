package org.d3javu
package domain.business

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

  @newtype
  final case class OfficialName(asString: String)

  @newtype
  final case class Brand(asString: String)

  @newtype
  final case class Email(asString: String)

  @newtype
  final case class PasswordHash(asString: String)

}