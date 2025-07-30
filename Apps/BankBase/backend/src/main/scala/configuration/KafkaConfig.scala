package configuration

import zio.config.typesafe.TypesafeConfigProvider
import zio.{Config, ZLayer}
import zio.Layer
import zio.config.magnolia.deriveConfig

final case class KafkaConfig(
                            bootstrapServers: String
                            )

object KafkaConfig {
  val layer: Layer[Config.Error, KafkaConfig] =
    ZLayer(TypesafeConfigProvider.fromResourcePath().kebabCase.load(deriveConfig[KafkaConfig].nested("kafka")))
}