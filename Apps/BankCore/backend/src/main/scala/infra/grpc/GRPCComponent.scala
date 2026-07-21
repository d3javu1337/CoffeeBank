package org.d3javu
package infra.grpc

import org.d3javu.backend.grpc.Transactions.{InvoiceServiceFs2Grpc, TransactionServiceFs2Grpc}
import org.d3javu.infra.config.types.GRPCConfig
import org.d3javu.service.transaction.impl.TransactionServiceImpl
import cats.{Applicative, Parallel}
import cats.effect.Async
import cats.syntax.all._
import cats.effect.kernel.Resource
import cats.effect.std.Dispatcher
import cats.implicits.catsSyntaxApplicativeId
import fs2.grpc.syntax.all._
import io.grpc.netty.shaded.io.grpc.netty.{NettyChannelBuilder, NettyServerBuilder}
import io.grpc.{ManagedChannel, Metadata}
import org.typelevel.log4cats.Logger

class GRPCComponent[F[_]: Async: Applicative: Logger: Parallel](config: GRPCConfig) {

  lazy val channel: Resource[F, ManagedChannel] = {
    NettyChannelBuilder.forAddress(config.address, config.port).usePlaintext()
      .resource[F]
  }

  def run(
      invoiceService: InvoiceServiceFs2Grpc[F, Metadata],
      transactionService: TransactionServiceFs2Grpc[F, Metadata],
  ): F[Nothing] = {
    Dispatcher.parallel[F].flatMap(dp => {
      NettyServerBuilder
        .forPort(config.port)
        .addService(InvoiceServiceFs2Grpc.bindService(dp, invoiceService))
        .addService(TransactionServiceFs2Grpc.bindService(dp, transactionService))
        .resource[F]
        .evalMap(server => Async[F].delay(server.start()).void)
        .evalMap(_ => Logger[F].info(s"grpc server started at ${config.port}"))
        .evalMap(_ => Async[F].never)
    }).useForever
  }

}
