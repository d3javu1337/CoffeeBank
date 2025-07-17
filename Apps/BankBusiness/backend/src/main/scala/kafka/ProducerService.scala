package kafka

import configuration.KafkaConfig
import org.apache.kafka.clients.producer.RecordMetadata
import kafka.messages.businessclient.BusinessClientCreateRequest
import kafka.messages.contactperson.{ContactPersonCreateRequest, ContactPersonDeleteRequest, ContactPersonUpdateRequest}
import kafka.messages.paymentaccount.PaymentAccountCreateRequest
import util.KafkaMessage
import zio.kafka.producer.{Producer, ProducerSettings}
import zio.kafka.*
import zio.kafka.serde.{Serde, Serializer}
import zio.{RIO, RLayer, Scope, Task, ZIO, ZLayer}
import util.given

case class ProducerService(private val config: KafkaConfig) {

  private val producer: RIO[Scope, Producer] = Producer.make(
    ProducerSettings(config.bootstrapServers.split("\\s+|,+").toList)
  )

  def produce(message: KafkaMessage): RIO[Scope, RIO[Any, RecordMetadata]] = message match {
    case m: BusinessClientCreateRequest => {
      producer.map(p => p.produce(
        topic = "business-client_registration_topic",
        key = "",
        value = m,
        keySerializer = Serde.string,
        valueSerializer = Serde.json[Any, BusinessClientCreateRequest]
      ))
    }
    case m: ContactPersonCreateRequest => {
      producer.map(p => p.produce(
        topic = "contact-person_create_topic",
        key = "",
        value = m,
        keySerializer = Serde.string,
        valueSerializer = Serde.json[Any, ContactPersonCreateRequest]
      ))
    }
    case m: ContactPersonDeleteRequest => {
      producer.map(p => p.produce(
        topic = "contact-person_delete_topic",
        key = "",
        value = m,
        keySerializer = Serde.string,
        valueSerializer = Serde.json[Any, ContactPersonDeleteRequest]
      ))
    }
    case m: ContactPersonUpdateRequest => {
      producer.map(p => p.produce(
        topic = "contact-person_update_topic",
        key = "",
        value = m,
        keySerializer = Serde.string,
        valueSerializer = Serde.json[Any, ContactPersonUpdateRequest]
      ))
    }
    case m: PaymentAccountCreateRequest => {
      producer.map(p => p.produce(
        topic = "payment-account_create_topic",
        key = "",
        value = m,
        keySerializer = Serde.string,
        valueSerializer = Serde.json[Any, PaymentAccountCreateRequest]
      ))
    }
  }
}

object ProducerService {
  val layer: RLayer[KafkaConfig, ProducerService] = ZLayer.fromFunction(ProducerService.apply _)
}