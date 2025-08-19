package kafka

import configuration.{KafkaConfig, MailConfig, ServerConfig}
import emailing.Emailer
import mongo.EmailConfirmDocumentDAL
import service.MailService
import zio.{RLayer, Scope, ZIO, ZLayer}
import zio.kafka.consumer.{Consumer, ConsumerSettings, Subscription}
import zio.kafka.serde.Serde

class KafkaConsumer(private val config: KafkaConfig) {
  private val consumer = Consumer.make(
    ConsumerSettings(config.bootstrapServers.split("[,\\s*]").toList)
      .withGroupId(config.groupId)
      .withMaxPollRecords(10)
  )

  def consume: ZIO[EmailConfirmDocumentDAL & MailService & MailConfig & Emailer & ServerConfig, Throwable, Unit] = ZIO.scoped {
    consumer
      .flatMap(_.consumeWith(
        subscription = Subscription.topics(config.consumerTopic),
        keyDeserializer = Serde.string,
        valueDeserializer = Serde.string
      )(record =>
        ZIO.serviceWithZIO[MailService](_.sendEmailConfirmation(record.value())).orDie
      ))
  }
}

object KafkaConsumer {
  val live: RLayer[KafkaConfig & EmailConfirmDocumentDAL & MailService, KafkaConsumer] = ZLayer.derive[KafkaConsumer]
}
