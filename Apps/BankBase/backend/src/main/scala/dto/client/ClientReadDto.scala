package dto.client

import zio.json.{DeriveJsonCodec, JsonCodec}

case class ClientReadDto(
                        id: Long,
                        surname: String,
                        name: String,
                        patronymic: String
                        )

object ClientReadDto {
  inline given JsonCodec[ClientReadDto] = DeriveJsonCodec.gen
}