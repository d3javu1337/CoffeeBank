package kafka.messages.contactperson

import zio.json.{DeriveJsonCodec, DeriveJsonDecoder, DeriveJsonEncoder, JsonCodec, JsonDecoder, JsonEncoder}

case class ContactPersonCreateRequest(
                                       businessClientEmail: String,
                                       surname: String,
                                       name: String,
                                       patronymic: String,
                                       phoneNumber: String,
                                       email: String
                                     )

object ContactPersonCreateRequest {
  implicit val encoder: JsonEncoder[ContactPersonCreateRequest] =
    DeriveJsonEncoder.gen[ContactPersonCreateRequest]

  implicit val decoder: JsonDecoder[ContactPersonCreateRequest] =
    DeriveJsonDecoder.gen[ContactPersonCreateRequest]
    
  implicit val codec: JsonCodec[ContactPersonCreateRequest] = DeriveJsonCodec.gen[ContactPersonCreateRequest]
}