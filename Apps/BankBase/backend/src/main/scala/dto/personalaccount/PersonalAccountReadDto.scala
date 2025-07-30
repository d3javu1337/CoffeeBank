package dto.personalaccount

import model.personalaccount.AccountType
import zio.json.{DeriveJsonCodec, JsonCodec}


case class PersonalAccountReadDto(
                                 id: Long,
                                 name: String,
                                 deposit: Double,
                                 accountType: AccountType
                                 )

object PersonalAccountReadDto {
  inline given JsonCodec[PersonalAccountReadDto] = DeriveJsonCodec.gen
}