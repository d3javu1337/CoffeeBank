package org.d3javu
package infra.config.types
import pureconfig._
import pureconfig.generic.auto._
import pureconfig.generic.semiauto._

case class KafkaConfig(main: MainKafkaConfig, util: UtilKafkaConfig)

case class MainKafkaConfig(
    consumer: ConsumerConfig,
    baseTopics: BaseTopics,
    businessTopics: BusinessTopics,
)

case class BaseTopics(
    clientRegistration: String,
    accountCreate: String,
    accountRename: String,
    cardCreate: String,
    cardRename: String,
)

case class BusinessTopics(
    clientRegistration: String,
    paymentAccountCreate: String,
    contactPersonCreate: String,
    contactPersonUpdate: String,
    contactPersonDelete: String,
)

case class UtilKafkaConfig(producer: ProducerConfig, consumer: ConsumerConfig, requestTopic: String, responseTopic: String)

case class ConsumerConfig(bootstrapServers: String, groupId: String)

case class ProducerConfig(bootstrapServers: String, groupId: String)
