package org.d3javu
package infra

import infra.grpc.GRPCComponent

import cats.Parallel
import cats.effect.implicits.effectResourceOps
import org.d3javu.infra.kafka.KafkaComponent
import org.d3javu.service.ServiceComponent
import org.d3javu.service.base.BaseServiceComponent
import cats.{Applicative, Monad}
import cats.effect.kernel.{Async, Resource}
import cats.syntax.all._
import cats.syntax.applicative.catsSyntaxApplicativeId
import cats.implicits._
import org.d3javu.infra.config.AppConfig
import org.d3javu.infra.config.types.GRPCConfig
import cats.mtl.syntax.all._
import cats.mtl.implicits._
import org.typelevel.log4cats.Logger

class Infrastructure[F[_] : Async](
                                    val grpcService: GRPCComponent[F],
                                    val kafkaComponent: KafkaComponent[F]
                                             )

private class AppInfrastructure[F[_] : Applicative : Monad : Logger : Async: Parallel](config: AppConfig) {

  def build: Resource[F, Infrastructure[F]] = {
    for {
      _ <- Logger[F].info("init core infra").toResource
      kafka <- KafkaComponent.make[F](config.kafka)
      grpc = new GRPCComponent[F](config.grpc)
      res = new Infrastructure[F](
        grpc,
        kafka
      )
      _ <- Logger[F].info("finish core infra init").toResource
    } yield res
  }

}

object AppInfrastructure {

  def make[F[_] : Async : Logger: Parallel](
                                   config: AppConfig
                                 ): Resource[F, Infrastructure[F]] = {
    new AppInfrastructure[F](config).build
  }

}