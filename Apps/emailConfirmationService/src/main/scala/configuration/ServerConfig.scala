package configuration

import zio.{Config, Layer, ZLayer}
import zio.config.magnolia.deriveConfig
import zio.config.typesafe.TypesafeConfigProvider

case class ServerConfig(
                       port: Int
                       ) {}

object ServerConfig {
  val layer: Layer[Config.Error, ServerConfig] =
    ZLayer(TypesafeConfigProvider.fromResourcePath().kebabCase.load(deriveConfig[ServerConfig].nested("server")))
}