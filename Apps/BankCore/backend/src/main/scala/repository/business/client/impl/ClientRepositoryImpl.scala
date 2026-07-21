package org.d3javu
package repository.business.client.impl

import org.d3javu.domain.business.Client
import org.d3javu.repository.business.client.impl.ClientRepositoryImpl.{findIdByEmailQuery, registrationCommand}
import repository.business.client.ClientRepository

import cats.effect.Sync
import cats.syntax.all._
import shapeless.HNil
import skunk.codec.all._
import skunk.implicits._
import skunk.implicits.toStringOps
import skunk.syntax.all._
import skunk.{*:, Command, Query, Session}

private[repository] class ClientRepositoryImpl[F[_]: Sync](session: Session[F])
    extends ClientRepository[F] {
  override def registration(
      officialName: Client.OfficialName,
      brand: Client.Brand,
      email: Client.Email,
      passwordHash: Client.PasswordHash,
  ): F[Client.Id] = (for {
    command <- session.prepare(registrationCommand)
    id <- command.unique(
      officialName.asString ::
      brand.asString ::
      email.asString ::
      passwordHash.asString ::
        HNil
    )
  } yield Client.Id(id)).adaptError { case err => repository.DBError(err) }

  override def findIdByEmail(email: Client.Email): F[Option[Client.Id]] = (for {
    query <- session.prepare(findIdByEmailQuery)
    id <- query.option(email.asString)
  } yield id.map(Client.Id.apply)).adaptError { case err => repository.DBError(err) }
}

object ClientRepositoryImpl {

  def make[F[_]: Sync](session: Session[F]): ClientRepositoryImpl[F] =
    new ClientRepositoryImpl[F](session)

  val registrationCommand: Query[String *: String *: String *: String *: HNil, Long] =
    sql"""insert into business_client (official_name, brand, email, password_hash)
          values ($varchar, $varchar, $varchar, $varchar)
          returning id
       """.query(int8)

  val findIdByEmailQuery: Query[String, Long] =
    sql"select bc.id from business_client bc where bc.email = $varchar"
      .query(int8)

}
