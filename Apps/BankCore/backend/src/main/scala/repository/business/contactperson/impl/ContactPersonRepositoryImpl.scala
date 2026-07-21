package org.d3javu
package repository.business.contactperson.impl

import org.d3javu.domain.business.{Client, ContactPerson}
import org.d3javu.repository.business.contactperson.impl.ContactPersonRepositoryImpl.{createCommand, deleteCommand, updateCommand}
import repository.business.contactperson.ContactPersonRepository

import cats.effect.Sync
import skunk.codec.all._
import skunk.implicits.toStringOps
import skunk.syntax.all._
import cats.syntax.all._
import shapeless.HNil
import skunk.{*:, Command, Query, Session}

class ContactPersonRepositoryImpl[F[_]: Sync](session: Session[F])
    extends ContactPersonRepository[F] {
  override def create(
      businessClientId: Client.Id,
      surname: ContactPerson.Surname,
      name: ContactPerson.Name,
      patronymic: ContactPerson.Patronymic,
      phoneNumber: ContactPerson.PhoneNumber,
      email: ContactPerson.Email,
  ): F[ContactPerson.Id] = (for {
    command <- session.prepare(createCommand)
    id <- command.unique(
      surname.asString ::
      name.asString ::
      patronymic.asString ::
      phoneNumber.asString ::
      email.asString ::
      businessClientId.asLong ::
        HNil
    )
  } yield ContactPerson.Id(id)).adaptError { case err => repository.DBError(err) }

  override def update(
      businessClientId: Client.Id,
      id: ContactPerson.Id,
      surname: ContactPerson.Surname,
      name: ContactPerson.Name,
      patronymic: ContactPerson.Patronymic,
      phoneNumber: ContactPerson.PhoneNumber,
      email: ContactPerson.Email,
  ): F[Unit] = (for {
    command <- session.prepare(updateCommand)
    _ <- command.execute(
      surname.asString ::
      name.asString ::
      patronymic.asString ::
      phoneNumber.asString ::
      email.asString ::
      id.asLong ::
      businessClientId.asLong ::
        HNil
    )
  } yield ()).adaptError { case err => repository.DBError(err) }

  override def delete(
      businessClientId: Client.Id,
      personId: ContactPerson.Id,
  ): F[Unit] = (for {
    command <- session.prepare(deleteCommand)
    _ <- command.execute(personId.asLong :: businessClientId.asLong :: HNil)
  } yield ()).adaptError { case err => repository.DBError(err) }
}

object ContactPersonRepositoryImpl {

  def make[F[_]: Sync](session: Session[F]): ContactPersonRepositoryImpl[F] =
    new ContactPersonRepositoryImpl[F](session)

  val createCommand
      : Query[String *: String *: String *: String *: String *: Long *: HNil, Long] =
    sql"""insert into contact_person (surname, name, patronymic, phone_number, email, business_client_id)
          values ($varchar, $varchar, $varchar, $varchar, $varchar, $int8)
          returning id
       """.query(int8)

  val updateCommand
      : Command[String *: String *: String *: String *: String *: Long *: Long *: HNil] =
    sql"""update contact_person
          set surname = $varchar, name = $varchar, patronymic = $varchar, phone_number = $varchar, email = $varchar
          where id = $int8 and business_client_id = $int8
       """.command

  val deleteCommand: Command[Long *: Long *: HNil] =
    sql"delete from contact_person where id = $int8 and business_client_id = $int8 "
      .command

}
