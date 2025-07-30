package web.requests

import zio.json.{DeriveJsonCodec, JsonCodec}
import zio.schema.codec.BinaryCodec
import zio.schema.codec.JsonCodec.schemaBasedBinaryCodec
import zio.schema.{DeriveSchema, Schema}

import java.time.LocalDate

case class ClientRegistration(
                               surname: String,
                               name: String,
                               patronymic: String,
                               dateOfBirth: LocalDate,
                               phoneNumber: String,
                               email: String,
                               password: String,
                             )

object ClientRegistration {
  inline given Schema[ClientRegistration] = DeriveSchema.gen[ClientRegistration]
  inline given BinaryCodec[ClientRegistration] = schemaBasedBinaryCodec
}