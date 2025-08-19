package configuration

import zio.config.magnolia.deriveConfig
import zio.config.typesafe.TypesafeConfigProvider
import zio.{Config, Layer, ZLayer}

case class MongoConfig(
                        username: String,
                        password: String,
                        host: String,
                        port: Long,
                        authenticationDatabase: String,
                        database: String,
                        collection: String
                      ){}

object MongoConfig {
  val layer: Layer[Config.Error, MongoConfig] =
    ZLayer(TypesafeConfigProvider.fromResourcePath().kebabCase.load(deriveConfig[MongoConfig]().nested("mongo")))
}