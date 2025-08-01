package service

import dao.postgres.repository.{PersonalAccountRepository, TransactionRepository}
import dto.transaction.TransactionReadDto
import errors.NoEntityPresented
import org.d3javu.backend.grpc.transactions.{InvoicePaymentRequest, TransferByPhoneNumberRequest}
import org.d3javu.backend.grpc.transactions.ZioTransactions.TransactionServiceClient
import service.TransactionService.{GetTransactionByIdEnv, GetTransactionsPageableEnv}
import util.PageRequest
import zio.{RIO, ULayer, ZIO, ZLayer}

import java.util.UUID

case class TransactionService() {
  def transferByPhoneNumber(recipientPhoneNumber: String, senderAccountId: Long, amount: Double): RIO[TransactionServiceClient, Boolean] = {
    ZIO.succeed(
        TransferByPhoneNumberRequest(
          recipientPhoneNumber,
          senderAccountId,
          amount
        ))
      .flatMap(request => TransactionServiceClient.transferByPhoneNumber(request))
      .flatMap(res => ZIO.succeed(res.isCompleted))
  }

  def purchase(invoiceNumber: String, personalAccountId: Long): RIO[TransactionServiceClient, Boolean] = {
    ZIO.succeed(
      InvoicePaymentRequest(
        personalAccountId,
        invoiceNumber
      ))
      .flatMap(request => TransactionServiceClient.invoicePayment(request))
      .flatMap(res => ZIO.succeed(res.isCompleted))
  }

  def getTransactionsPageable(clientEmail: String,
                              pageNumber: Option[Int], pageSize: Option[Int]): RIO[GetTransactionsPageableEnv, List[TransactionReadDto]] = for {
    pageRequest <- ZIO.succeed(PageRequest(pageNumber.getOrElse(0), pageSize.getOrElse(30)))
    accountId <- ZIO.serviceWithZIO[PersonalAccountService](_.getAccountIdByClientEmail(clientEmail))
    res <- ZIO.serviceWithZIO[TransactionRepository](_.findAllTransactionsPageable(accountId, pageRequest))
  } yield res

  def getTransactionById(clientEmail: String, transactionId: String): RIO[GetTransactionByIdEnv, TransactionReadDto] = {
    ZIO.serviceWithZIO[PersonalAccountService](_.getAccountIdByClientEmail(clientEmail)).flatMap(id =>
    ZIO.serviceWithZIO[TransactionRepository](_.findTransactionById(id, UUID.fromString(transactionId))))
      .someOrFail(NoEntityPresented())
  }

}

object TransactionService {

  private type GetTransactionByIdEnv = TransactionRepository &
    PersonalAccountService &
    PersonalAccountRepository

  private type GetTransactionsPageableEnv = TransactionRepository &
    PersonalAccountService &
    PersonalAccountRepository

  val layer: ULayer[TransactionService] = ZLayer.derive[TransactionService]
}