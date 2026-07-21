package org.d3javu
package infra.kafka.main.dto.base

import io.circe._
import io.circe.generic.semiauto._

import java.time.LocalDate

object ClientRequests {

  case class Registration(
      surname: String,
      name: String,
      patronymic: String,
      dateOfBirth: LocalDate,
      phoneNumber: String,
      email: String,
      passwordHash: String,
      isActive: Boolean = false,
  )

  implicit val decoder: Decoder[Registration] = deriveDecoder[Registration]
}
