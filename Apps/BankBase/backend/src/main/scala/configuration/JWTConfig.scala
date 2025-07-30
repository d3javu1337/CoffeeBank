package configuration

import zio.{Config, Layer, ZLayer}
import zio.config.magnolia.deriveConfig
import zio.config.typesafe.TypesafeConfigProvider

final case class JWTConfig(
                    accessSecret: String,
                    refreshSecret: String
                    )

object JWTConfig {
  val layer: Layer[Config.Error, JWTConfig] =
    ZLayer(TypesafeConfigProvider.fromResourcePath().kebabCase.load(deriveConfig[JWTConfig].nested("jwt")))
}