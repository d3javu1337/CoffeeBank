package org.d3javu
package infra.kafka

import infra.kafka.main.MainKafkaService

import cats.effect.implicits.effectResourceOps
import cats.effect.implicits._
import cats.syntax.all._
import cats.effect.implicits._
import cats.effect.{Async, Resource}
import org.d3javu.infra.config.types.KafkaConfig
import org.d3javu.infra.kafka.util.UtilKafkaService
import org.typelevel.log4cats.Logger

class KafkaComponent[F[_]](
                    val main: MainKafkaService[F],
                    val util: UtilKafkaService[F]
                    ) {}

object KafkaComponent {
  def make[F[_]: Async: Logger](config: KafkaConfig): Resource[F, KafkaComponent[F]] = for {
    _ <- Logger[F].info("start init kafka").toResource
    mainKafka = (new MainKafkaService[F](config.main))
    utilKafka = (new UtilKafkaService[F](config.util))
    comp = new KafkaComponent[F](
      mainKafka,
      utilKafka
    )
    _ <- Logger[F].info("finis init kafka").toResource
  } yield comp
}