package kafka

import org.apache.kafka.clients.producer.RecordMetadata
import zio.{RIO, RLayer, ZLayer}
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

}
