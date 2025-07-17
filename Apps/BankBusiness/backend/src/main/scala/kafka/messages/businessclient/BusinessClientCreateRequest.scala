package kafka.messages.businessclient

import zio.json.{DeriveJsonCodec, DeriveJsonDecoder, DeriveJsonEncoder, JsonCodec, JsonDecoder, JsonEncoder}

case class BusinessClientCreateRequest(
                                        officialName: String,
                                        brand: String,
                                        email: String,
                                        passwordHash: String
                                      )

object BusinessClientCreateRequest {
  implicit val encoder: JsonEncoder[BusinessClientCreateRequest] =
    DeriveJsonEncoder.gen[BusinessClientCreateRequest]

  implicit val decoder: JsonDecoder[BusinessClientCreateRequest] =
    DeriveJsonDecoder.gen[BusinessClientCreateRequest]

  implicit val codec: JsonCodec[BusinessClientCreateRequest] = DeriveJsonCodec.gen[BusinessClientCreateRequest]
}