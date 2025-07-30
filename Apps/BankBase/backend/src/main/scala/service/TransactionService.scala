package service

import io.grpc.StatusException
import org.d3javu.backend.grpc.transactions.{InvoicePaymentRequest, TransferByPhoneNumberRequest}
import org.d3javu.backend.grpc.transactions.ZioTransactions.TransactionServiceClient
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
}

object TransactionService {
  val layer: ULayer[TransactionService] = ZLayer.derive[TransactionService]
}