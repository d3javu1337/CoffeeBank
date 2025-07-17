package web.request.invoice

import zio.schema.{DeriveSchema, Schema}
import zio.schema.codec.BinaryCodec

import java.util.UUID

case class InvoiceIssueRequest(
                                token: UUID,
                                amount: Double
                              )

object InvoiceIssueRequest {
  implicit val schema: Schema[InvoiceIssueRequest] = DeriveSchema.gen[InvoiceIssueRequest]

  implicit val jsonBinaryCodec: BinaryCodec[InvoiceIssueRequest] =
    zio.schema.codec.JsonCodec.schemaBasedBinaryCodec(schema)
}