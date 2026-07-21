package org.d3javu
package repository.base.client.impl

import java.time.LocalDate

import org.d3javu.domain.base.Client
import org.d3javu.domain.base.Client.{Email, PhoneNumber}
import org.d3javu.infra.kafka.main.dto.base.ClientRequests.Registration

import repository.base.client.ClientRepository
import cats.effect.Sync
import cats.syntax.all._
import skunk.codec.all._
import skunk.implicits.toStringOps
import skunk.syntax.all._
import skunk.{Codec, Command, Query, Session}
import ClientRepositoryImpl.{
  clientRegistrationCommand, confirmEmailCommand, findClientByPhoneNumberQuery,
}

private[repository] class ClientRepositoryImpl[F[_]: Sync](session: Session[F])
    extends ClientRepository[F] {

  override def registration(registrationDto: Registration): F[Unit] = (for {
    command <- session.prepare(clientRegistrationCommand)
    _ <- command.execute(registrationDto)
  } yield ()).adaptError { case err => repository.DBError(err) }

  override def confirmEmail(email: Email): F[Unit] = (for {
    command <- session.prepare(confirmEmailCommand)
    _ <- command.execute(email.asString)
  } yield ()).adaptError { case err => repository.DBError(err) }

  override def findClientByPhoneNumber(
      phoneNumber: PhoneNumber,
  ): F[Option[Client.Id]] = (for {
    query <- session.prepare(findClientByPhoneNumberQuery)
    res <- query.option(phoneNumber.asString)
  } yield res.map(Client.Id.apply)).adaptError { case err =>
    repository.DBError(err)
  }
}

private[repository] object ClientRepositoryImpl {

  def make[F[_]: Sync](session: Session[F]): ClientRepositoryImpl[F] =
    new ClientRepositoryImpl[F](session)

  private val findClientByPhoneNumberQuery: Query[String, Long] =
    sql"select c.id from client c where c.phone_number = $varchar".query(int8)

  private val confirmEmailCommand: Command[String] =
    sql"update client set is_enabled = true where email = $varchar".command

  private val clientRegistrationCommand: Command[Registration] =
    sql"""insert into
          client(surname, name, patronymic, date_of_birth, phone_number, email, password_hash, is_enabled)
          values ($varchar, $varchar, $varchar, $date, $varchar, $varchar, $varchar, $bool)
       """.command.to[Registration]

}
