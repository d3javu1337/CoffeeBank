package org.d3javu
package infra.kafka.main.dto.business

import domain.business.Client

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

object ClientRequests {

  case class Registration(
      officialName: Client.OfficialName,
      brand: Client.Brand,
      email: Client.Email,
      passwordHash: Client.PasswordHash,
  )

  object Registration {
    implicit val decoder: Decoder[Registration] = deriveDecoder[Registration]
    implicit val encoder: Encoder[Registration] = deriveEncoder[Registration]
  }

}
