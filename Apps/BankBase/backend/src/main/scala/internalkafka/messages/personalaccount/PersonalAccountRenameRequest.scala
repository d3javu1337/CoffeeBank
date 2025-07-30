package internalkafka.messages.personalaccount

import zio.json.{DeriveJsonCodec, DeriveJsonDecoder, DeriveJsonEncoder, JsonCodec, JsonDecoder, JsonEncoder}

case class PersonalAccountRenameRequest(
                                         clientId: Long,
                                         email: String,
                                         accountId: Long,
                                         newName: String
                                       )

object PersonalAccountRenameRequest {
  inline given JsonCodec[PersonalAccountRenameRequest] = DeriveJsonCodec.gen[PersonalAccountRenameRequest]
}
