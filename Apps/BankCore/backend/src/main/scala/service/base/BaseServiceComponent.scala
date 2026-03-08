package org.d3javu
package service.base

import service.base.card.CardService

import cats.Applicative
import cats.effect.{Resource, Sync}
import cats.syntax.all._
import cats.effect.syntax.all._
import org.d3javu.repository.base.BaseRepositoryComponent
import org.d3javu.service.base.card.impl.CardServiceImpl
import org.d3javu.service.base.client.ClientService
import org.d3javu.service.base.client.impl.ClientServiceImpl
import org.d3javu.service.base.personalaccount.PersonalAccountService
import org.typelevel.log4cats.Logger

class BaseServiceComponent[F[_]](
//                             cardService: CardService[F],
                             val clientService: ClientService[F],
//                             personalAccountService: PersonalAccountService[F]
                          ) {

}

object BaseServiceComponent {
  def make[F[_]: Logger: Applicative: Sync](baseRepositoryComponent: BaseRepositoryComponent[F]): Resource[F, BaseServiceComponent[F]] = for {

    _ <- Logger[F].info("started BaseServiceComponent init").toResource

    clientService = new ClientServiceImpl[F](baseRepositoryComponent.clientRepository)

    _ <- Logger[F].info("finished BaseServiceComponent init").toResource

    comp <- Resource.eval(
      Sync[F].delay(
        new BaseServiceComponent[F](
          clientService,
        )
      )
    )

  } yield comp
}