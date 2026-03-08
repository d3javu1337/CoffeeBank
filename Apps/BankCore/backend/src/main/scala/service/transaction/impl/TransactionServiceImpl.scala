package org.d3javu
package service.transaction.impl


import cats.Applicative
import cats.syntax.all._
import cats.implicits._
import io.grpc._
import fs2.Stream
import io.grpc.Metadata
import org.d3javu.backend.grpc.Transactions.{InvoicePaymentRequest, InvoicePaymentResponse, TransactionServiceFs2Grpc, TransferByPhoneNumberRequest, TransferByPhoneNumberResponse}
import org.d3javu.service.transaction.TransactionService

import scala.concurrent.Future

class TransactionServiceImpl[F[_]: Applicative] extends TransactionServiceFs2Grpc[F, Metadata] {
  override def transferByPhoneNumber(
                                      request: TransferByPhoneNumberRequest,
                                      ctx: Metadata
                                    ): F[TransferByPhoneNumberResponse] = {
    ???
//    TransferByPhoneNumberResponse(isCompleted = false).pure[F]
  }

  override def invoicePayment(request: InvoicePaymentRequest, ctx: Metadata): F[InvoicePaymentResponse] = ???
  //  override def transferByPhoneNumber: F[Unit] = ???
  //
  //  override def invoicePayment: F[Unit] = ???
}
