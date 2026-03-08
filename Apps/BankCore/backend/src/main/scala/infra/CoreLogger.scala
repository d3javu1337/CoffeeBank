package org.d3javu
package infra

import cats.Applicative
import cats.effect.kernel.Sync
import cats.syntax.all._
import cats.implicits.toFunctorOps
import cats.implicits.catsSyntaxApplicativeId
import org.typelevel.log4cats.{Logger, LoggerFactory, SelfAwareStructuredLogger}
import org.typelevel.log4cats.slf4j.{Slf4jFactory, Slf4jLogger}

class CoreLogger[F[_]: Sync: Applicative] {

  private implicit val factory: LoggerFactory[F] = Slf4jFactory.create[F]

//  override def getLoggerFromName(name: String): SelfAwareStructuredLogger[F] = factory.getLoggerFromName(name)
//
//  override def fromName(name: String): F[SelfAwareStructuredLogger[F]] = getLoggerFromName(name).pure[F]

  def loggerForName(name: String): Logger[F] = factory.getLoggerFromName(name)

}
