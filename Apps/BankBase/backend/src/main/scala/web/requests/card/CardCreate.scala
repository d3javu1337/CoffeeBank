package web.requests.card

import model.card.CardType
import zio.json.{DeriveJsonCodec, JsonCodec}
import zio.schema.codec.BinaryCodec
import zio.schema.codec.JsonCodec.schemaBasedBinaryCodec
import zio.schema.{DeriveSchema, Schema}

case class CardCreate(cardType: CardType) {
  require(cardType != null, "card type can not be null")
}

object CardCreate {
  inline given Schema[CardCreate] = DeriveSchema.gen
  inline given BinaryCodec[CardCreate] = schemaBasedBinaryCodec
}
