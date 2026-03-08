package org.d3javu
package infra.config.types
import pureconfig._
import pureconfig.generic.semiauto._
import pureconfig.generic.auto._

case class GRPCConfig(
                     address: String,
                     port: Int,
                     )
