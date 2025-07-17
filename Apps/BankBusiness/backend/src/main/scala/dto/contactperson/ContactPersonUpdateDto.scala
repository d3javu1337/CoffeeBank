package dto.contactperson

import zio.schema.{DeriveSchema, Schema}
import zio.schema.codec.BinaryCodec

case class ContactPersonUpdateDto(
                                   id: Long,
                                   surname: String,
                                   name: String,
                                   patronymic: String,
                                   phoneNumber: String,
                                   email: String

                                 )

object ContactPersonUpdateDto {
  implicit val schema: Schema[ContactPersonUpdateDto] = DeriveSchema.gen[ContactPersonUpdateDto]

  implicit val jsonBinaryCodec: BinaryCodec[ContactPersonUpdateDto] =
    zio.schema.codec.JsonCodec.schemaBasedBinaryCodec(schema)
}
