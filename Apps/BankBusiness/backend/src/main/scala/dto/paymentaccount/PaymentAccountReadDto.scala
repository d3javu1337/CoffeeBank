package dto.paymentaccount

import zio.json.{DeriveJsonCodec, DeriveJsonDecoder, DeriveJsonEncoder, JsonCodec, JsonDecoder, JsonEncoder}

case class PaymentAccountReadDto(
                                id: Long,
                                name: String,
                                deposit: Double
                                )


object PaymentAccountReadDto {
  implicit val encoder: JsonEncoder[PaymentAccountReadDto] = DeriveJsonEncoder.gen[PaymentAccountReadDto]
  implicit val decoder: JsonDecoder[PaymentAccountReadDto] = DeriveJsonDecoder.gen[PaymentAccountReadDto]
  given JsonCodec[PaymentAccountReadDto] = DeriveJsonCodec.gen[PaymentAccountReadDto]
}