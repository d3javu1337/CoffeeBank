package internalkafka.messages.client

import zio.json.{DeriveJsonCodec, DeriveJsonDecoder, DeriveJsonEncoder, JsonCodec, JsonDecoder, JsonEncoder}

import java.time.LocalDate

case class ClientCreateRequest(
                                surname: String,
                                name: String,
                                patronymic: String,
                                dateOfBirth: LocalDate,
                                phoneNumber: String,
                                email: String,
                                passwordHash: String,
                              )

object ClientCreateRequest {
  inline given JsonCodec[ClientCreateRequest] = DeriveJsonCodec.gen[ClientCreateRequest]
}
