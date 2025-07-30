package configuration

import zio.{Config, Layer, ZLayer}
import zio.config.magnolia.deriveConfig
import zio.config.typesafe.TypesafeConfigProvider

case class GrpcConfig(
                       host: String,
                       port: Int
                     ) {}

object GrpcConfig {
  val layer: Layer[Config.Error, GrpcConfig] =
    ZLayer(TypesafeConfigProvider.fromResourcePath().kebabCase.load(deriveConfig[GrpcConfig].nested("grpc")))
}