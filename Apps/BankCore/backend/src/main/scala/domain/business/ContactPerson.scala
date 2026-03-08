package org.d3javu
package domain.business

import io.estatico.newtype.macros.newtype
import org.d3javu.domain.business.ContactPerson._

object ContactPerson {

  @newtype
  final case class Id(asLong: Long)

  @newtype
  final case class Surname(asString: String)

  @newtype
  final case class Name(asString: String)

  @newtype
  final case class Patronymic(asString: String)

  @newtype
  final case class PhoneNumber(asString: String)

  @newtype
  final case class Email(asString: String)

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
