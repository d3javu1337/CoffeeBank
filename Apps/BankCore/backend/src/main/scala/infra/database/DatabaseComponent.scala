package org.d3javu
package infra.database

import scala.concurrent.duration.DurationInt

import cats.implicits._
import cats.syntax.all._
import infra.config.types.PostgresConfig
import cats.effect.Temporal
import cats.effect.kernel.Resource
import cats.effect.std.Console
import fs2.io.net.Network
import natchez.Trace
import skunk.{Session, Strategy}

object DatabaseComponent {

  def pooledConnection[F[_]: Temporal: Trace: Network: Console](
      config: PostgresConfig,
  ): Resource[F, Resource[F, Session[F]]] = Session.pooled(
    host = config.host,
    port = config.port,
    user = config.username,
    database = config.database,
    password = Some(config.password),
    max = 10,
    readTimeout = 1.seconds,
  )

}
