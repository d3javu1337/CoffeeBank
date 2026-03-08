package org.d3javu
package service

import service.base.BaseServiceComponent

import cats.effect.implicits.effectResourceOps
import cats.effect.kernel.Sync
import cats.{Applicative, Parallel}
import cats.effect.{Async, Resource}
import org.d3javu.infra.Infrastructure
import org.d3javu.infra.grpc.GRPCComponent
import org.d3javu.repository.RepositoryComponent
import org.d3javu.service.business.BusinessServiceComponent
import org.typelevel.log4cats.Logger

class ServiceComponent[F[_]: Async: Applicative](
                      val base: BaseServiceComponent[F],
//                      business: BusinessServiceComponent[F],
//                      GRPCService: GRPCService[F]
                      ) {

}

object ServiceComponent {

  def make[F[_]: Async: Applicative: Parallel: Logger: Sync](
                                                        infra: Infrastructure[F],
                                                        repositoryComponent: RepositoryComponent[F]
                                                      ): Resource[F,ServiceComponent[F]] = for {
    _ <- Logger[F].info("service").toResource
    repos <- Resource.eval(Sync[F].delay(repositoryComponent))
    base <- BaseServiceComponent.make[F](repos.baseRepositoryComponent)
  } yield new ServiceComponent(base)

}