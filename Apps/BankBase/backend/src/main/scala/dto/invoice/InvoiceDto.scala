package dto.invoice

import zio.json.{DeriveJsonCodec, JsonCodec}

case class InvoiceDto(
                     amount: Double
                     )

object InvoiceDto {
  inline given JsonCodec[InvoiceDto] = DeriveJsonCodec.gen
}