package org.d3javu
package infra.config

import infra.config.types._

import pureconfig._
import pureconfig.generic.auto._
import pureconfig.generic.semiauto.deriveReader

case class AppConfig(
                    grpc: GRPCConfig,
                    kafka: KafkaConfig,
                    postgres: PostgresConfig
                    )

object AppConfig {
  implicit val appConfigReader: ConfigReader[AppConfig] = deriveReader[AppConfig]
}