package dto.contactperson

import zio.schema.{DeriveSchema, Schema}
import zio.schema.codec.BinaryCodec

case class ContactPersonCreateDto(
                                   surname: String,
                                   name: String,
                                   patronymic: String,
                                   phoneNumber: String,
                                   email: String,
                                 )

object ContactPersonCreateDto {
  implicit val schema: Schema[ContactPersonCreateDto] = DeriveSchema.gen[ContactPersonCreateDto]

  implicit val jsonBinaryCodec: BinaryCodec[ContactPersonCreateDto] =
    zio.schema.codec.JsonCodec.schemaBasedBinaryCodec(schema)
}
