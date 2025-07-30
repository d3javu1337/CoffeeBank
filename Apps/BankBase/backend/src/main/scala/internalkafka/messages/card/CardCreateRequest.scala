package internalkafka.messages.card

import model.card.CardType
import zio.json.{DeriveJsonCodec, JsonCodec}

case class CardCreateRequest(
                              clientId: Long,
                              email: String,
                              accountId: Long,
                              cardType: CardType,
                              name: String
                            )

object CardCreateRequest {
  inline given JsonCodec[CardCreateRequest] = DeriveJsonCodec.gen[CardCreateRequest]
}
