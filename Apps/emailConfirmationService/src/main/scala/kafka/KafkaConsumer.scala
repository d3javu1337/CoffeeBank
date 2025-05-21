package kafka

import service.MailService
import zio.RIO
import zio.kafka.consumer.{Consumer, ConsumerSettings, Subscription}
import zio.kafka.serde.Serde

object KafkaConsumer {
  def consume(bootstrapServers: List[String], groupId: String, topic: String): RIO[Any, Any] = {
    Consumer.consumeWith(
      settings = ConsumerSettings(bootstrapServers)
        .withGroupId(groupId)
        .withMaxPollRecords(10),
      subscription = Subscription.topics(topic),
      keyDeserializer = Serde.string,
      valueDeserializer = Serde.string
    )(record => {
      MailService.sendEmailConfirmation(record.value()).orDie
    })
  }
}
