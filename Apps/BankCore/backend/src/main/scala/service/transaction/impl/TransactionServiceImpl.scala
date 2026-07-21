package org.d3javu
package service.transaction.impl

import org.d3javu.backend.grpc.Transactions.{
  InvoicePaymentRequest, InvoicePaymentResponse, TransactionServiceFs2Grpc,
  TransferByPhoneNumberRequest, TransferByPhoneNumberResponse,
}
import org.d3javu.service.transaction.TransactionService

import cats.Applicative
import cats.effect.Async
import cats.implicits._
import cats.syntax.all._
import io.grpc._
import fs2.Stream

class TransactionServiceImpl[F[_]: Async]
    extends TransactionService[F] {
  override def transferByPhoneNumber(
      request: TransferByPhoneNumberRequest,
      ctx: Metadata,
  ): F[TransferByPhoneNumberResponse] = {
    Async[F].raiseError(
      Status.UNIMPLEMENTED.withDescription("Unimplemented method")
        .asRuntimeException(),
    )
  }

  override def invoicePayment(
      request: InvoicePaymentRequest,
      ctx: Metadata,
  ): F[InvoicePaymentResponse] = {
    Async[F].raiseError(
      Status.UNIMPLEMENTED.withDescription("Unimplemented method")
        .asRuntimeException(),
    )

  }
}

object TransactionServiceImpl {
  def make[F[_]: Async]: TransactionServiceImpl[F] =
    new TransactionServiceImpl[F]
}
