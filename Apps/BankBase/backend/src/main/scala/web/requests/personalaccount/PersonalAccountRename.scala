package web.requests.personalaccount

import zio.schema.codec.BinaryCodec
import zio.schema.codec.JsonCodec.schemaBasedBinaryCodec
import zio.schema.{DeriveSchema, Schema}

case class PersonalAccountRename(
                                accountId: Long,
                                newName: String
                                ) {
  require(newName != null)
}

object PersonalAccountRename {
  inline given Schema[PersonalAccountRename] = DeriveSchema.gen
  inline given BinaryCodec[PersonalAccountRename] = schemaBasedBinaryCodec
}