package org.d3javu
package repository.base.client.impl

import java.time.LocalDate
import org.d3javu.domain.base.Client
import repository.base.client.ClientRepository

import cats.effect.Sync
import cats.syntax.all._
import skunk.codec.all._
import skunk.implicits.toStringOps
import skunk.syntax.all._
import skunk.{Codec, Command, Query, Session}
import ClientRepositoryImpl.{clientRegistrationCommand, confirmEmailCommand, findClientByPhoneNumberQuery}
import org.d3javu.domain.base.Client.{Email, PhoneNumber}
import org.d3javu.infra.kafka.main.dto.ClientRequests.RegistrationDto

class ClientRepositoryImpl[F[_]: Sync](session: Session[F])
    extends ClientRepository[F] {

  override def registration(registrationDto: RegistrationDto): F[Unit] = for {
    command <- session.prepare(clientRegistrationCommand)
    res <- command.execute(registrationDto)
  } yield res

  override def confirmEmail(email: Email): F[Unit] = for {
    command <- session.prepare(confirmEmailCommand)
    res <- command.execute(email.asString)
  } yield res

  override def findClientByPhoneNumber(phoneNumber: PhoneNumber): F[Option[Client.Id]] =
    for {
      query <- session.prepare(findClientByPhoneNumberQuery)
      res <- query.option(phoneNumber.asString)
    } yield res.map(Client.Id.apply)
}

private object ClientRepositoryImpl {

  val findClientByPhoneNumberQuery: Query[String, Long] =
    sql"select c.id from client c where c.phone_number = $varchar".query(int8)

  val confirmEmailCommand: Command[String] =
    sql"update client set is_enabled = true where email = $varchar".command

  val clientRegistrationCommand: Command[RegistrationDto] = {
    sql"""insert into
          client(surname, name, patronymic, date_of_birth, phone_number, email, password_hash, is_enabled)
          values ($varchar, $varchar, $varchar, $date, $varchar, $varchar, $varchar, $bool)
       """
      .command
      .to[RegistrationDto]
  }

}
