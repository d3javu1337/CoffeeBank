package org.d3javu
package infra.grpc

import cats.Applicative
import cats.effect.Async
import cats.effect.kernel.Resource
import cats.implicits.catsSyntaxApplicativeId
import fs2.grpc.syntax.all._
import io.grpc.{ManagedChannel, Metadata}
import io.grpc.netty.shaded.io.grpc.netty.{NettyChannelBuilder, NettyServerBuilder}
import org.d3javu.infra.config.types.GRPCConfig
import org.d3javu.service.transaction.impl.TransactionServiceImpl


class GRPCComponent[F[_]: Async: Applicative](
                                config: GRPCConfig
                              ) {

  lazy val channel: Resource[F, ManagedChannel] = {
    NettyChannelBuilder
      .forAddress(
        config.address,
        config.port
      )
      .usePlaintext()
      .resource[F]
  }

  def run() = NettyServerBuilder
    .forPort(config.port)
    .resource[F]
    .evalMap(_.start().pure[F])
    .useForever

}
