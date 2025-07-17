package dto.payment

import zio.json.{DeriveJsonCodec, DeriveJsonDecoder, DeriveJsonEncoder, JsonCodec, JsonDecoder, JsonEncoder}

import java.util.UUID

case class PaymentWithAmountDto(paymentId: UUID, amount: Double)

object PaymentWithAmountDto {
  implicit val encoder: JsonEncoder[PaymentWithAmountDto] = DeriveJsonEncoder.gen[PaymentWithAmountDto]
  implicit val decoder: JsonDecoder[PaymentWithAmountDto] = DeriveJsonDecoder.gen[PaymentWithAmountDto]
  given JsonCodec[PaymentWithAmountDto] = DeriveJsonCodec.gen[PaymentWithAmountDto]
}