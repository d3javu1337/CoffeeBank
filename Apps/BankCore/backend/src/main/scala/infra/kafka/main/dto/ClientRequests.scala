package org.d3javu
package infra.kafka.main.dto

import fs2.kafka.Deserializer
import io.circe._
import io.circe.generic.semiauto._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import java.time.LocalDate

object ClientRequests {

  case class RegistrationDto(
      surname: String,
      name: String,
      patronymic: String,
      dateOfBirth: LocalDate,
      phoneNumber: String,
      email: String,
      passwordHash: String,
      isActive: Boolean = false,
  )


//  implicit val deserializer: Decoder[RegistrationDto] = deriveDecoder[RegistrationDto]
//  implicit val deserializer: Deserializer[RegistrationDto] = new Deserializer[RegistrationDto] {
//    override def deserialize(topic: String, data: Array[Byte]): RegistrationDto = bytesDeserializer
//  }

}
