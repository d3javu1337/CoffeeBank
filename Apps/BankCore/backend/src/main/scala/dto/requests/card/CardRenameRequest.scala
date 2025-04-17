package dto.requests.card

case class CardRenameRequest(accountId: Long, cardId: Long, newName: String)
