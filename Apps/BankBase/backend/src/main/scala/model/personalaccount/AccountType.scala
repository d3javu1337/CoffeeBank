package model.personalaccount

import zio.json.{DeriveJsonCodec, JsonCodec}

enum AccountType(defaultName: String) {
  case PERSONAL extends AccountType("Личный счет")
}

object AccountType {
  inline given JsonCodec[AccountType] = DeriveJsonCodec.gen
}