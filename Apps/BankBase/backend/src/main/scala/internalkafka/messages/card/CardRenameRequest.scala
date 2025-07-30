package internalkafka.messages.card

import zio.json.{DeriveJsonCodec, DeriveJsonDecoder, DeriveJsonEncoder, JsonCodec, JsonDecoder, JsonEncoder}

case class CardRenameRequest(
                              clientId: Long,
                              email: String,
                              accountId: Long,
                              cardId: Long,
                              newName: String
                            )

object CardRenameRequest {
  inline given JsonCodec[CardRenameRequest] = DeriveJsonCodec.gen[CardRenameRequest]
}
