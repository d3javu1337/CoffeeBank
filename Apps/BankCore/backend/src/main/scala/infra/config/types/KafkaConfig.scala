package org.d3javu
package infra.config.types
import pureconfig._
import pureconfig.generic.semiauto._
import pureconfig.generic.auto._

case class KafkaConfig(
                      main: MainKafkaConfig,
                      util: UtilKafkaConfig
                      )

case class MainKafkaConfig(
                          consumer: ConsumerConfig
                          )

case class UtilKafkaConfig(
                          producer: ProducerConfig,
                          consumer: ConsumerConfig
                          )

case class ConsumerConfig(
                           bootstrapServers: String,
                           groupId: String
                         )
case class ProducerConfig(
                           bootstrapServers: String,
                           groupId: String
                         )