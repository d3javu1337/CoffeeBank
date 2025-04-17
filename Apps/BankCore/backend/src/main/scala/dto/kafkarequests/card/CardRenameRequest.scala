package dto.kafkarequests.card

case class CardRenameRequest(clientId: Long, email: String, accountId: Long, cardId: Long, newName: String)
