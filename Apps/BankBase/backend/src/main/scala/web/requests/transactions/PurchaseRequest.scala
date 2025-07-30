package web.requests.transactions

import zio.schema.codec.BinaryCodec
import zio.schema.codec.JsonCodec.schemaBasedBinaryCodec
import zio.schema.{DeriveSchema, Schema}

case class PurchaseRequest(
                          invoiceId: String,
                          accountId: Long
                          )

object PurchaseRequest {
  inline given Schema[PurchaseRequest] = DeriveSchema.gen
  inline given BinaryCodec[PurchaseRequest] = schemaBasedBinaryCodec
}