package web.requests

import zio.schema.codec.BinaryCodec
import zio.schema.codec.JsonCodec.schemaBasedBinaryCodec
import zio.schema.{DeriveSchema, Schema}

case class Login(email: String, password: String)

object Login {
  inline given Schema[Login] = DeriveSchema.gen[Login]
  inline given BinaryCodec[Login] = schemaBasedBinaryCodec
}