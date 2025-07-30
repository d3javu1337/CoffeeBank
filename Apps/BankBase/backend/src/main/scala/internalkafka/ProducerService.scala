package internalkafka

import configuration.KafkaConfig
import internalkafka.ProducerService.KafkaMessage
import internalkafka.messages.card.{CardCreateRequest, CardRenameRequest}
import internalkafka.messages.client.ClientCreateRequest
import internalkafka.messages.personalaccount.{PersonalAccountCreateRequest, PersonalAccountRenameRequest}
import zio.kafka.producer.{Producer, ProducerSettings}
import zio.kafka.serde.Serde
import zio.{RIO, RLayer, Scope, ZIO, ZLayer}
import model.card.CardType.given
import org.apache.kafka.clients.producer.RecordMetadata
import util.ZIOExtensions.*
import zio.Scope

case class ProducerService(private val config: KafkaConfig) {
  private val producer = Producer.make(
    ProducerSettings(config.bootstrapServers.split("\\s+|,+").toList)
  )

  def produce(message: KafkaMessage): RIO[Scope, RIO[Any, RecordMetadata]] = message match {
    case m: CardCreateRequest => producer.map(_.produce(
      topic = "card-create-topic",
      key = "",
      value = m,
      keySerializer = Serde.string,
      valueSerializer = Serde.json[Any, CardCreateRequest]
    )).customRetry(5, 100)
    case m: CardRenameRequest => producer.map(_.produce(
      topic = "card-rename-topic",
      key = "",
      value = m,
      keySerializer = Serde.string,
      valueSerializer = Serde.json[Any, CardRenameRequest]
    )).customRetry(5, 100)
    case m: ClientCreateRequest => producer.map(_.produce(
      topic = "client-registration-topic",
      key = "",
      value = m,
      keySerializer = Serde.string,
      valueSerializer = Serde.json[Any, ClientCreateRequest]
    )).customRetry(5, 100)
    case m: PersonalAccountCreateRequest => producer.map(_.produce(
      topic = "account-create-topic",
      key = "",
      value = m,
      keySerializer = Serde.string,
      valueSerializer = Serde.json[Any, PersonalAccountCreateRequest]
    )).customRetry(5, 100)
    case m: PersonalAccountRenameRequest => producer.map(_.produce(
      topic = "account-rename-topic",
      key = "",
      value = m,
      keySerializer = Serde.string,
      valueSerializer = Serde.json[Any, PersonalAccountRenameRequest]
    )).customRetry(5, 100)
  }

}

object ProducerService {

  private type KafkaMessage =
    CardCreateRequest |
      CardRenameRequest |
      ClientCreateRequest |
      PersonalAccountCreateRequest |
      PersonalAccountRenameRequest

  val layer: RLayer[KafkaConfig, ProducerService] = ZLayer.fromFunction(ProducerService.apply _)
}