package configuration

import zio.{Config, ZLayer}
import zio.config.magnolia.{DeriveConfig, deriveConfig}
import zio.config.typesafe.TypesafeConfigProvider

case class KafkaConfig(bootstrapServers: String)

object KafkaConfig {
  val layer: ZLayer[Any, Config.Error, KafkaConfig] =
    ZLayer(TypesafeConfigProvider.fromResourcePath().kebabCase.load(deriveConfig[KafkaConfig].nested("kafka")))
}