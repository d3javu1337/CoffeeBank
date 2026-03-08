package org.d3javu
package repository.base

import org.d3javu.repository.base.client.impl.ClientRepositoryImpl

import repository.base.client.ClientRepository
import cats.effect.Sync
import skunk.Session

class BaseRepositoryComponent[F[_]: Sync](val clientRepository: ClientRepository[F]) {}

object BaseRepositoryComponent {
  def make[F[_]: Sync](session: Session[F]): BaseRepositoryComponent[F] = {
    new BaseRepositoryComponent[F](new ClientRepositoryImpl[F](session))
  }
}
