package org.d3javu
package service.base

import org.d3javu.repository.base.BaseRepositoryComponent
import org.d3javu.service.base.card.impl.CardServiceImpl
import org.d3javu.service.base.client.ClientService
import org.d3javu.service.base.client.impl.ClientServiceImpl
import org.d3javu.service.base.personalaccount.PersonalAccountService
import org.typelevel.log4cats.Logger
import service.base.card.CardService

import cats.Applicative
import cats.effect.syntax.all._
import cats.effect.{Async, Resource, Sync}
import cats.syntax.all._
import org.d3javu.infra.kafka.util.UtilKafkaService
import org.d3javu.service.base.personalaccount.impl.PersonalAccountServiceImpl
import org.d3javu.service.secutiry.SecurityUtilService

class BaseServiceComponent[F[_]](
    val cardService: CardService[F],
    val clientService: ClientService[F],
    val personalAccountService: PersonalAccountService[F],
) {}

object BaseServiceComponent {
  def make[F[_]: Logger: Applicative: Sync: Async](
      baseRepositoryComponent: BaseRepositoryComponent[F],
      utilKafkaService: UtilKafkaService[F],
      securityUtilService: SecurityUtilService
  ): Resource[F, BaseServiceComponent[F]] = for {

    _ <- Logger[F].info("started BaseServiceComponent init").toResource

    clientService =
      new ClientServiceImpl[F](baseRepositoryComponent.clientRepository, utilKafkaService)
    paService = new PersonalAccountServiceImpl[F](baseRepositoryComponent.personalAccountRepo)
    cardService = new CardServiceImpl[F](securityUtilService, paService, baseRepositoryComponent.cardRepository)

    _ <- Logger[F].info("finished BaseServiceComponent init").toResource

    comp <- Resource.eval(Sync[F].delay(
      new BaseServiceComponent[F](cardService, clientService, paService),
    ))

  } yield comp
}