package web.requests.transactions

import zio.json.JsonCodec
import zio.schema.codec.BinaryCodec
import zio.schema.codec.JsonCodec.schemaBasedBinaryCodec
import zio.schema.{DeriveSchema, Schema}

case class TransferByPhoneNumberRequest(
                                       phoneNumber: String,
                                       accountId: Long,
                                       amount: Double
                                       )
object TransferByPhoneNumberRequest {
  inline given Schema[TransferByPhoneNumberRequest] = DeriveSchema.gen
  inline given BinaryCodec[TransferByPhoneNumberRequest] = schemaBasedBinaryCodec
}