package dto.kafkarequests.account

case class AccountRenameRequest(clientId: Long, email: String, accountId: Long, newName: String)
