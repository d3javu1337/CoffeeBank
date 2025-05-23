package kafka

import org.apache.kafka.clients.producer.RecordMetadata
import zio.kafka.admin.AdminClient.KafkaConfig
import zio.{RIO, RLayer, Scope, ZIO, ZLayer}
import zio.kafka.producer.{Producer, ProducerSettings}
import zio.kafka.serde.Serde

object KafkaProducer {
  def produce(topic: String, key: String, value: String): RIO[Any with Producer, RecordMetadata] = {
    Producer.produce[Any, String, String](
      topic = topic,
      key = key,
      value = value,
      keySerializer = Serde.string,
      valueSerializer = Serde.string
    )
  }

  def producerLayer(bootstrapServers: List[String]): RLayer[Any, Producer] = {
    ZLayer.scoped(
      Producer.make(
        ProducerSettings().withBootstrapServers(bootstrapServers)
      )
    )
  }

//  val layer: RLayer[KafkaConfig, ] = ZLayer.scoped(
//    ZIO.serviceWithZIO[KafkaConfig](config => Producer.make(ProducerSettings(config.bootstrapServers)))
//  ) >>> ZLayer.derive[EventPublisherImplementation]

}
