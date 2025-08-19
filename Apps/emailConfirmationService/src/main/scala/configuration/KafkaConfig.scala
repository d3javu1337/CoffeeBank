package configuration

import zio.{Config, Layer, ZLayer}
import zio.config.magnolia.deriveConfig
import zio.config.typesafe.TypesafeConfigProvider

case class KafkaConfig(
                        bootstrapServers: String,
                        groupId: String,
                        consumerTopic: String,
                        producerTopic: String
                      ) {}

object KafkaConfig {
  val layer: Layer[Config.Error, KafkaConfig] =
    ZLayer(TypesafeConfigProvider.fromResourcePath().kebabCase.load(deriveConfig[KafkaConfig].nested("kafka")))
}