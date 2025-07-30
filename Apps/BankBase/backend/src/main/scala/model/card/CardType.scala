package model.card

import zio.json.{DeriveJsonCodec, JsonCodec}

enum CardType(val defaultCardName: String) {
  case CREDIT extends CardType("Кредитная карта")
  case DEBIT extends CardType("Дебетовая карта")
  case OVERDRAFT extends CardType("Карта с овердрафтом")
  case PREPAID extends CardType("Предоплаченная карта")
}

object CardType {
  inline given JsonCodec[CardType] = DeriveJsonCodec.gen[CardType]
}