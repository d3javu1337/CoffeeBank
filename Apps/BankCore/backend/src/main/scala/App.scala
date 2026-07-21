package org.d3javu

import scala.concurrent.duration.DurationInt
import org.apache.kafka.common.serialization.Deserializer
import org.d3javu.domain.base.Client.PhoneNumber
import org.d3javu.infra.config.AppConfig
import org.d3javu.infra.database.DatabaseComponent
import org.d3javu.infra.kafka.main.dto.base.ClientRequests.Registration
import org.d3javu.infra.{AppInfrastructure, CoreLogger, Infrastructure}
import org.d3javu.repository.RepositoryComponent
import org.d3javu.service.ServiceComponent
import org.typelevel.log4cats.Logger
import cats.effect.implicits.effectResourceOps
import cats.effect.kernel.syntax.EffectResourceOps
import cats.effect.std.Console
import cats.effect.{Async, IO, Resource, Sync}
import cats.implicits.catsSyntaxApplicativeId
import cats.syntax.all._
import cats.{MonadThrow, Parallel}
import pureconfig.ConfigSource
import natchez.Trace
import org.d3javu.domain.base.Client

class AppComponent[F[_]: Async: Sync: Logger: Parallel: Trace: Console](
    val infra: Infrastructure[F],
    val services: ServiceComponent[F],
    val repo: RepositoryComponent[F],
)

object App {

  def build[F[_]: Async: Sync: Logger: Parallel: Trace: Console: MonadThrow]
      : Resource[F, AppComponent[F]] = for {
    _ <- Logger[F].info("init app started").toResource
    config <- Resource
      .pure(ConfigSource.default.at("application").loadOrThrow[AppConfig])

    postgresConn <- DatabaseComponent.pooledConnection(config.postgres)
    session <- postgresConn
      .onError(_ => Logger[F].error("connect error").toResource)
    repoComponent = RepositoryComponent.make[F](session)

    infra <- AppInfrastructure.make[F](config)

    services <- ServiceComponent.make[F](infra, repoComponent)

    // base handlers
    _ <- infra.kafkaComponent.main.startConsume(
      config.kafka.main.baseTopics.clientRegistration,
      services.base.clientService.registration,
    ).toResource

    _ <- infra.kafkaComponent.main.startConsume(
      config.kafka.main.baseTopics.cardCreate,
      services.base.cardService.createCard,
    ).toResource

    _ <- infra.kafkaComponent.main.startConsume(
      config.kafka.main.baseTopics.cardRename,
      services.base.cardService.renameCard,
    ).toResource

    _ <- infra.kafkaComponent.main.startConsume(
      config.kafka.main.baseTopics.accountCreate,
      services.base.personalAccountService.createAccount,
    ).toResource

    _ <- infra.kafkaComponent.main.startConsume(
      config.kafka.main.baseTopics.accountRename,
      services.base.personalAccountService.renameAccount,
    ).toResource


    // business handlers
    _ <- infra.kafkaComponent.main.startConsume(
      config.kafka.main.businessTopics.clientRegistration,
      services.business.clientService.registration,
    ).toResource

    _ <- infra.kafkaComponent.main.startConsume(
      config.kafka.main.businessTopics.paymentAccountCreate,
      services.business.paymentAccountService.createPaymentAccount,
    ).toResource

    _ <- infra.kafkaComponent.main.startConsume(
      config.kafka.main.businessTopics.contactPersonCreate,
      services.business.contactPersonService.create,
    ).toResource

    _ <- infra.kafkaComponent.main.startConsume(
      config.kafka.main.businessTopics.contactPersonUpdate,
      services.business.contactPersonService.update,
    ).toResource

    _ <- infra.kafkaComponent.main.startConsume(
      config.kafka.main.businessTopics.contactPersonDelete,
      services.business.contactPersonService.delete,
    ).toResource

    _ <- infra.kafkaComponent.util.startConsume(
      config.kafka.util.responseTopic,
      email => services.base.clientService.confirmEmail(Client.Email(email))
    ).toResource

    _ <- Resource.eval(infra.grpcService.run(
      services.business.invoiceService,
      services.transactionService,
    ))


    _ <- Logger[F].info("init app finished").toResource
  } yield new AppComponent[F](infra, services, repoComponent)

}
