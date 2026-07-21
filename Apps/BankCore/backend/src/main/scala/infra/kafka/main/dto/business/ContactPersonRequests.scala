package org.d3javu
package infra.kafka.main.dto.business

import org.d3javu.domain.business.ContactPerson._
import domain.business.Client

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
object ContactPersonRequests {

  case class Create(
      businessClientEmail: Client.Email,
      surname: Surname,
      name: Name,
      patronymic: Patronymic,
      phoneNumber: PhoneNumber,
      email: Email,
  )

  object Create {
    implicit val createDecoder: Decoder[Create] = deriveDecoder[Create]
  }

  case class Update(
      businessClientEmail: Client.Email,
      contactPersonId: Id,
      surname: Surname,
      name: Name,
      patronymic: Patronymic,
      phoneNumber: PhoneNumber,
      email: Email,
  )

  object Update {
    implicit val updateDecoder: Decoder[Update] = deriveDecoder[Update]
  }

  case class Delete(businessClientEmail: Client.Email, personId: Id)

  object Delete {
    implicit val deleteDecoder: Decoder[Delete] = deriveDecoder[Delete]
  }

}
