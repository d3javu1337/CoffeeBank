package internalkafka.messages.personalaccount

import zio.json.{DeriveJsonCodec, DeriveJsonDecoder, DeriveJsonEncoder, JsonCodec, JsonDecoder, JsonEncoder}

case class PersonalAccountCreateRequest(
                                         id: Long,
                                         email: String
                                       )

object PersonalAccountCreateRequest {
  inline given JsonCodec[PersonalAccountCreateRequest] = DeriveJsonCodec.gen[PersonalAccountCreateRequest]
}
