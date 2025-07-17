package dto.businessclient

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

case class BusinessClientReadDto(
                                officialName: String,
                                brand: String
                                )

object BusinessClientReadDto {
  implicit val encoder: JsonEncoder[BusinessClientReadDto] = DeriveJsonEncoder.gen[BusinessClientReadDto]
  implicit val decoder: JsonDecoder[BusinessClientReadDto] = DeriveJsonDecoder.gen[BusinessClientReadDto]
}