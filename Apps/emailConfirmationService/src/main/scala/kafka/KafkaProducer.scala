package kafka

import configuration.KafkaConfig
import org.apache.kafka.clients.producer.RecordMetadata
import zio.{RIO, RLayer, Scope, ZIO, ZLayer}
import zio.kafka.producer.{Producer, ProducerSettings}
import zio.kafka.serde.Serde

class KafkaProducer(private val config: KafkaConfig) {

  private val producer = Producer.make(ProducerSettings(config.bootstrapServers.split(",\\s*").toList))

  def produce(email: String): RIO[Scope, RIO[Any, RecordMetadata]] = producer
    .map(p => p.produce(
      topic = config.producerTopic,
      key = "",
      value = email,
      keySerializer = Serde.string,
      valueSerializer = Serde.string
    ))
}

object KafkaProducer {
  val live: RLayer[KafkaConfig, KafkaProducer] = ZLayer.derive[KafkaProducer]
}