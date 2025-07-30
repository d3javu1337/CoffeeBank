package dto.card

import model.card.CardType
import zio.json.{DeriveJsonCodec, JsonCodec}

case class CardReadDto(
                      id: Long,
                      name: String,
                      cardType: CardType,
                      number: String
                      )

object CardReadDto {
  inline given JsonCodec[CardReadDto] = DeriveJsonCodec.gen
}