package org.d3javu
package repository

import cats.effect.implicits.effectResourceOps
import cats.effect.{Resource, Sync}
import cats.syntax.all._
import cats.implicits.catsSyntaxApplicativeId
import org.d3javu.repository.base.BaseRepositoryComponent
import org.typelevel.log4cats.Logger
import skunk.Session

class RepositoryComponent[F[_]: Sync](val baseRepositoryComponent: BaseRepositoryComponent[F]) {

}

object RepositoryComponent {
  def make[F[_]: Sync: Logger](session: Session[F]): RepositoryComponent[F] = {
    (new RepositoryComponent[F](
      BaseRepositoryComponent.make[F](session)
    ))
//    _ <- Logger[F].info("")
  }
}