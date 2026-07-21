package org.d3javu
package repository.base

import org.d3javu.repository.base.client.impl.ClientRepositoryImpl
import repository.base.client.ClientRepository

import cats.effect.Sync
import org.d3javu.repository.base.card.CardRepository
import org.d3javu.repository.base.card.impl.CardRepositoryImpl
import org.d3javu.repository.base.personalaccount.PersonalAccountRepository
import org.d3javu.repository.base.personalaccount.impl.PersonalAccountRepositoryImpl
import skunk.Session

class BaseRepositoryComponent[F[_]](
                                           val clientRepository: ClientRepository[F],
                                           val cardRepository: CardRepository[F],
                                           val personalAccountRepo: PersonalAccountRepository[F],
                                         ) {}

object BaseRepositoryComponent {
  def make[F[_]: Sync](session: Session[F]): BaseRepositoryComponent[F] = {
    new BaseRepositoryComponent[F](
      ClientRepositoryImpl.make[F](session),
      CardRepositoryImpl.make[F](session),
      PersonalAccountRepositoryImpl.make[F](session),
    )
  }
}
