package org.d3javu
package repository

import org.d3javu.repository.base.BaseRepositoryComponent
import org.d3javu.repository.business.BusinessRepositoryComponent
import org.typelevel.log4cats.Logger

import cats.effect.implicits.effectResourceOps
import cats.effect.{Resource, Sync}
import cats.implicits.catsSyntaxApplicativeId
import cats.syntax.all._
import skunk.Session

class RepositoryComponent[F[_]: Sync](
    val baseRepositoryComponent: BaseRepositoryComponent[F],
    val businessRepositoryComponent: BusinessRepositoryComponent[F],
) {}

object RepositoryComponent {
  def make[F[_]: Sync: Logger](session: Session[F]): RepositoryComponent[F] =
    new RepositoryComponent[F](
      BaseRepositoryComponent.make[F](session),
      BusinessRepositoryComponent.make[F](session),
    )
}
