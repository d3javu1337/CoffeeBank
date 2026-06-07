package dto.businessclient

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

case class BusinessClientCompact (
                                   id: Long,
                                   officialName: String,
                                   brand: String,
                                   email: String,
                                 )

object BusinessClientCompact {
  implicit val encoder: JsonEncoder[BusinessClientCompact] = DeriveJsonEncoder.gen[BusinessClientCompact]
  implicit val decoder: JsonDecoder[BusinessClientCompact] = DeriveJsonDecoder.gen[BusinessClientCompact]
}
