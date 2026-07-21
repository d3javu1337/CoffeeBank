package org.d3javu
package service

import service.base.BaseServiceComponent

import cats.effect.implicits.effectResourceOps
import cats.effect.Sync
import cats.{Applicative, Parallel}
import cats.effect.{Async, Resource}
import org.d3javu.infra.Infrastructure
import org.d3javu.infra.grpc.GRPCComponent
import org.d3javu.repository.RepositoryComponent
import org.d3javu.service.business.BusinessServiceComponent
import org.d3javu.service.secutiry.impl.SecurityUtilServiceImpl
import org.d3javu.service.transaction.TransactionService
import org.d3javu.service.transaction.impl.TransactionServiceImpl
import org.typelevel.log4cats.Logger

class ServiceComponent[F[_]: Async: Applicative](
                      val base: BaseServiceComponent[F],
                      val business: BusinessServiceComponent[F],
                      val transactionService: TransactionService[F]
                      ) {

}

object ServiceComponent {

  def make[F[_]: Async: Applicative: Parallel: Logger: Sync](
                                                        infra: Infrastructure[F],
                                                        repositoryComponent: RepositoryComponent[F]
                                                      ): Resource[F,ServiceComponent[F]] = for {
    _ <- Logger[F].info("service").toResource
    repos <- Resource.eval(Sync[F].delay(repositoryComponent))
    securityService = new SecurityUtilServiceImpl
    base <- BaseServiceComponent.make[F](repos.baseRepositoryComponent, infra.kafkaComponent.util, securityService)
    business <- BusinessServiceComponent.make[F](repos.businessRepositoryComponent)
    transaction = TransactionServiceImpl.make[F]
  } yield new ServiceComponent(base, business, transaction)

}