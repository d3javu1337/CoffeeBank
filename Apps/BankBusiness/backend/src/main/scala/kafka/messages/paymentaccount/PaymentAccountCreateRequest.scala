package kafka.messages.paymentaccount

import zio.json.{DeriveJsonCodec, JsonCodec}

case class PaymentAccountCreateRequest(
                                      clientId: Long,
                                      email: String
                                      )

object PaymentAccountCreateRequest {
  implicit val codec: JsonCodec[PaymentAccountCreateRequest] = DeriveJsonCodec.gen[PaymentAccountCreateRequest]
}