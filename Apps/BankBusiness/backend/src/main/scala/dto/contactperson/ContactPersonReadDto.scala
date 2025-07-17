package dto.contactperson

import zio.json.{DeriveJsonCodec, DeriveJsonDecoder, DeriveJsonEncoder, JsonCodec, JsonDecoder, JsonEncoder}

case class ContactPersonReadDto(
                               surname: String,
                               name: String,
                               patronymic: String,
                               phoneNumber: String,
                               email: String
                               )

object ContactPersonReadDto {
  implicit val encoder: JsonEncoder[ContactPersonReadDto] = DeriveJsonEncoder.gen[ContactPersonReadDto]
  implicit val decoder: JsonDecoder[ContactPersonReadDto] = DeriveJsonDecoder.gen[ContactPersonReadDto]
  given JsonCodec[ContactPersonReadDto] = DeriveJsonCodec.gen[ContactPersonReadDto]
}