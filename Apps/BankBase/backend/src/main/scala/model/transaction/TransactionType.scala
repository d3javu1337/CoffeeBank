package model.transaction

import zio.json.{DeriveJsonCodec, JsonCodec}

enum TransactionType(typeName: String){
  case PURCHASE extends TransactionType("Оплата")
  case TRANSFER extends TransactionType("Перевод")
  case WITHDRAW extends TransactionType("Снятие наличных")
  case REPLENISH extends TransactionType("Пополнение наличными")
}

object TransactionType {
  inline given JsonCodec[TransactionType] = DeriveJsonCodec.gen
}