package org.d3javu
package infra.kafka.util

import infra.config.types.UtilKafkaConfig

import fs2.kafka.ConsumerSettings
import io.circe.Decoder
import io.circe.parser.decode
import cats.effect._
import fs2.Stream._
import fs2.kafka._
import fs2.{Stream, kafka}
import cats.effect.implicits.effectResourceOps
import cats.syntax.all._
import org.d3javu.domain.base.Client.Email

class UtilKafkaService[F[_]: Async](
                                     config: UtilKafkaConfig,
                                     producer: KafkaProducer[F, String, String]
                                   ) {

  private val consumerSettings = ConsumerSettings[F, String, String]
    .withGroupId(config.consumer.groupId)
    .withBootstrapServers(config.consumer.bootstrapServers)
    .withEnableAutoCommit(true)

  private val producerSettings = ProducerSettings[F, String, String]
    .withBootstrapServers(config.producer.bootstrapServers)
    .withAcks(Acks.One)

  def produce(email: String): F[Unit] = {
    val record = ProducerRecord(config.requestTopic, email, email)
    producer.produce(ProducerRecords.one(record)).flatten.void
  }

  def startConsume(topic: String, func: String => F[_]): F[Unit] = {
    KafkaConsumer.stream[F, String, String](consumerSettings).subscribeTo(topic)
      .records.evalMap(commitable =>
        decode[String](commitable.record.value).liftTo[F]
          .map(dto => (commitable.offset, Option(dto)))
          .handleErrorWith(_ => (commitable.offset, Option.empty[String]).pure[F]),
      ).collect { case (offset, Some(dto)) => (offset, dto) }.evalMap {
        case (offset, dto) => func(dto) >> offset.commit
      }.compile.drain
  }

}

object UtilKafkaService {
  def make[F[_]: Async](config: UtilKafkaConfig): Resource[F, UtilKafkaService[F]] = {
    val producerSettings = ProducerSettings[F, String, String]
      .withBootstrapServers(config.producer.bootstrapServers)
      .withAcks(Acks.One)
    KafkaProducer.resource[F, String, String](producerSettings).map(pr =>
      new UtilKafkaService[F](config, pr)
    )
  }
}
