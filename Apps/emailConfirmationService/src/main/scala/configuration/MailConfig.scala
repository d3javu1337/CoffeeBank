package configuration

import zio.{Config, Layer, ZLayer}
import zio.config.magnolia.deriveConfig
import zio.config.typesafe.TypesafeConfigProvider

case class MailConfig(
                       host: String,
                       port: String,
                       auth: Boolean,
                       sslEnable: Boolean,
                       username: String,
                       password: String,
                       sender: String
                     ){}

object MailConfig {
  val layer: Layer[Config.Error, MailConfig] =
    ZLayer(TypesafeConfigProvider.fromResourcePath().kebabCase.load(deriveConfig[MailConfig].nested("emailing")))
}