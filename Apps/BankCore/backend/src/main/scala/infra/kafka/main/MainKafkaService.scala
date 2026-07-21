package org.d3javu
package infra.kafka.main

import scala.concurrent.duration.DurationInt
import org.d3javu.infra.kafka.main.dto.base.ClientRequests.Registration
import org.typelevel.log4cats.Logger

import infra.config.types.MainKafkaConfig
import cats.effect._
import fs2.Stream._
import fs2.kafka._
import fs2.{Stream, kafka}
import cats.effect.implicits.effectResourceOps
import cats.syntax.all._
import fs2.kafka.consumer.KafkaConsumeChunk.CommitNow
import io.circe.Decoder
import io.circe.parser.decode
import org.d3javu.infra.kafka.main.dto.base.AccountRequests

class MainKafkaService[F[_]: Async: Logger](config: MainKafkaConfig) {

  private val consumerSettings = ConsumerSettings[F, String, String]
    .withGroupId(config.consumer.groupId)
    .withBootstrapServers(config.consumer.bootstrapServers)
    .withAutoOffsetReset(AutoOffsetReset.Latest)
    .withEnableAutoCommit(true)

  def startConsume[T: Decoder](topic: String, func: T => F[_]): F[Unit] = {
    KafkaConsumer.stream[F, String, String](consumerSettings).subscribeTo(topic)
      .records.evalMap(commitable =>
      Logger[F].info(commitable.record.value) >>
        decode[T](commitable.record.value).liftTo[F]
          .map(dto => (commitable.offset, Option(dto)))
          .handleErrorWith(_ => (commitable.offset, Option.empty[T]).pure[F]),
      ).collect { case (offset, Some(dto)) => (offset, dto) }.evalMap {
        case (offset, dto) => func(dto) >> offset.commit
      }.compile.drain
  }
}

object MainKafkaService {
  def make[F[_]: Async: Logger](config: MainKafkaConfig): Resource[F, MainKafkaService[F]] = {
    Resource.eval(Async[F].delay(new MainKafkaService[F](config)))
  }
}
