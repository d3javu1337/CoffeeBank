package org.d3javu
package infra.kafka.main

import scala.concurrent.duration.DurationInt
import org.d3javu.infra.kafka.main.dto.AccountRequests
import org.typelevel.log4cats.Logger
import infra.config.types.MainKafkaConfig

import cats.effect.Async
import fs2.Stream
import fs2.kafka.{Deserializer, ValueDeserializer, _}
import cats.effect._
import cats.effect.implicits.effectResourceOps
import cats.syntax.all._
import fs2._
import fs2.Stream._
import fs2.kafka.consumer.KafkaConsumeChunk.CommitNow
import fs2.kafka
import org.d3javu.infra.kafka.main.dto.ClientRequests.RegistrationDto

class MainKafkaService[F[_]: Async: Logger](config: MainKafkaConfig) {

//  private val settings = KafkaConsumer.

  def consumer[T](topic: String) = {
    KafkaConsumer
      .stream[F, String, RegistrationDto](ConsumerSettings
        .apply[F, String, RegistrationDto]
        .withGroupId(config.consumer.groupId)
        .withBootstrapServers(config.consumer.bootstrapServers)
        .withEnableAutoCommit(true)
      )
      .subscribeTo(topic)
      .partitionedRecords
      .map(ps => ps.evalMap(t => Logger[F].info(t.record.value.toString)))
      .parJoinUnbounded
      .compile
      .drain
//    val instance = ConsumerApi.resource[F, String, T](
//      BootstrapServers(config.consumer.bootstrapServers),
//      GroupId(config.consumer.groupId),
//      EnableAutoCommit(true),
//    )
//    instance.map(_.subscribe(topic))
//    instance


  }

//  def t[T: Deserializer](topic: String, serializeTo: T) = {
//    consumer[T].map(_.subscribe(topic))
//  }

}
