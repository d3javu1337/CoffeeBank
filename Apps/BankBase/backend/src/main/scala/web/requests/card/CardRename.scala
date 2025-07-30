package web.requests.card

import zio.schema.codec.BinaryCodec
import zio.schema.codec.JsonCodec.schemaBasedBinaryCodec
import zio.schema.{DeriveSchema, Schema}

case class CardRename(
                     cardId: Long,
                     newName: String
                     )

object CardRename {
  inline given Schema[CardRename] = DeriveSchema.gen
  inline given BinaryCodec[CardRename] = schemaBasedBinaryCodec
}