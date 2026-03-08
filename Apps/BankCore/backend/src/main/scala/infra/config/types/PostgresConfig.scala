package org.d3javu
package infra.config.types
import pureconfig._
import pureconfig.generic.semiauto._
import pureconfig.generic.auto._

case class PostgresConfig(
                           host: String,
                           port: Int,
                           username: String,
                           password: String,
                           database: String
  )
