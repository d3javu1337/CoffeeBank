package web.request.auth

import zio.schema.codec.BinaryCodec
import zio.schema.{DeriveSchema, Schema}

case class BusinessClientRegistration(
                                officialName: String,
                                brand: String,
                                email: String,
                                password: String
                                ) {


}

object BusinessClientRegistration {
  implicit val schema: Schema[BusinessClientRegistration] = DeriveSchema.gen[BusinessClientRegistration]

  implicit val jsonBinaryCodec: BinaryCodec[BusinessClientRegistration] =
    zio.schema.codec.JsonCodec.schemaBasedBinaryCodec(schema)
}

