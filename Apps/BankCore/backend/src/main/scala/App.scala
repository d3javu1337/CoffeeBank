package org.d3javu

import cats.Parallel
import cats.effect.{Async, IO, Resource, Sync}
import cats.implicits.catsSyntaxApplicativeId
import cats.syntax.all._
import org.d3javu.infra.{AppInfrastructure, CoreLogger, Infrastructure}
import org.d3javu.infra.config.AppConfig
import org.typelevel.log4cats.Logger
import pureconfig.ConfigSource
import cats.effect.kernel.syntax.EffectResourceOps
import cats.effect.implicits.effectResourceOps
import cats.effect.std.Console
import natchez.Trace
import org.apache.kafka.common.serialization.Deserializer
import org.d3javu.domain.base.Client.PhoneNumber
import org.d3javu.infra.database.DatabaseComponent
import org.d3javu.infra.kafka.main.dto.ClientRequests.RegistrationDto
import org.d3javu.infra.kafka.main.dto.ClientRequests.RegistrationDto._
import org.d3javu.repository.RepositoryComponent
import org.d3javu.service.ServiceComponent

import scala.concurrent.duration.DurationInt
//import pureconfig.generic.auto._

class AppComponent[F[_]: Async: Sync: Logger: Parallel: Trace: Console](
    val infra: Infrastructure[F],
    val services: ServiceComponent[F],
    val repo: RepositoryComponent[F]
                   )

object App {

  def build[F[_]: Async: Sync: Logger: Parallel: Trace: Console]: Resource[F, AppComponent[F]] = {
    for {
      _ <- Logger[F].info("init app started").toResource
      config <- Resource.pure(ConfigSource.default
        .at("application")
        .loadOrThrow[AppConfig])

      postgresConn <- DatabaseComponent.pooledConnection(config.postgres)
      session <- postgresConn
      repoComponent = RepositoryComponent.make[F](session)

      infra <- AppInfrastructure.make[F](config)

      services <- ServiceComponent.make[F](infra, repoComponent)

//      implicit0(Deserializer[RegistrationDto]) <- RegistrationDto

      _ = infra.kafkaComponent.main.consumer[RegistrationDto]("")
        .map(_.recordStream(100.millis))
        .map(_.map(record => services.base.clientService.registration(record.value())))
        .toResource

      _ <- Logger[F].info("init app finished").toResource
    } yield new AppComponent[F](
      infra,
      services,
      repoComponent
    )
  }

}
