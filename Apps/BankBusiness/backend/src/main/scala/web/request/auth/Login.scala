package web.request.auth

import zio.schema.codec.BinaryCodec
import zio.schema.{DeriveSchema, Schema}

case class Login(email: String, password: String)

object Login{
    implicit val schema: Schema[Login] = DeriveSchema.gen
    
    implicit val jsonBinaryCodec: BinaryCodec[Login] =
        zio.schema.codec.JsonCodec.schemaBasedBinaryCodec(schema)
}
