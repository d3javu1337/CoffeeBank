package org.d3javu
package repository.base.personalaccount.impl

import org.d3javu.domain.base.{AccountType, Client, PersonalAccount}
import org.d3javu.domain.transaction.Transaction
import org.d3javu.repository.base.personalaccount.impl.PersonalAccountRepositoryImpl.{checkOwningQuery, createAccountCommand, findAccountByClientIdQuery, hasEnoughMoneyQuery, renameAccountCommand}
import repository.base.personalaccount.PersonalAccountRepository

import cats.effect.Sync
import skunk.codec.all._
import skunk.implicits._
import skunk.implicits.toStringOps
import skunk.syntax.all._
import skunk.{*:, Command, Query, Session}
import cats.syntax.all._
import shapeless.HNil

class PersonalAccountRepositoryImpl[F[_]: Sync](session: Session[F])
    extends PersonalAccountRepository[F] {
  override def checkOwning(
      accountId: PersonalAccount.AccountId,
      clientId: Client.Id,
      clientEmail: Client.Email,
  ): F[Boolean] = (for {
    query <- session.prepare(checkOwningQuery)
    res <- query.unique(accountId.asLong :: clientId.asLong :: clientEmail.asString :: HNil)
  } yield res).adaptError { case err => repository.DBError(err) }

  override def renameAccount(
      accountId: PersonalAccount.AccountId,
      newName: PersonalAccount.AccountName,
  ): F[Unit] = (for {
    command <- session.prepare(renameAccountCommand)
    _ <- command.execute(newName.asString :: accountId.asLong :: HNil)
  } yield ()).adaptError { case err => repository.DBError(err) }

  override def createAccount(
      clientId: Client.Id,
      accountType: AccountType,
      name: PersonalAccount.AccountName,
  ): F[Unit] = (for {
    command <- session.prepare(createAccountCommand)
    _ <- command.execute(clientId.asLong ::accountType.value :: name.asString :: HNil)
  } yield ()).adaptError { case err => repository.DBError(err) }

  override def findAccountByClientId(clientId: Client.Id): F[PersonalAccount.AccountId] = (for {
    query <- session.prepare(findAccountByClientIdQuery)
    res <- query.unique(clientId.asLong)
  } yield PersonalAccount.AccountId(res)).adaptError { case err => repository.DBError(err) }

  override def hasEnoughMoney(
      accountId: PersonalAccount.AccountId,
      money: Transaction.Money,
  ): F[Boolean] = (for {
    query <- session.prepare(hasEnoughMoneyQuery)
    res <- query.unique(money.asDouble :: accountId.asLong :: HNil)
  } yield res).adaptError { case err => repository.DBError(err) }
}

object PersonalAccountRepositoryImpl {
  def make[F[_]: Sync](session: Session[F]): PersonalAccountRepositoryImpl[F] =
    new PersonalAccountRepositoryImpl(session)

  private val checkOwningQuery: Query[Long *: Long *: String *: HNil, Boolean] =
    sql"""select count(*) > 0
          from personal_account a
          join client c
          on a.client_id = c.id
          where a.id = $int8 and a.client_id = $int8 and c.email = $varchar
       """.query(bool)

  private val renameAccountCommand: Command[String *: Long *: HNil] =
    sql"update personal_account set name = $varchar where id = $int8".command

  private val createAccountCommand: Command[Long *: String *: String *: HNil] =
    sql"""
          insert into
          personal_account(client_id, type, name)
          values ($int8, $varchar $varchar)
       """.command

  private val findAccountByClientIdQuery: Query[Long, Long] =
    sql"select a.id from personal_account a where a.client_id = $int8 limit 1"
      .query(int8)

  private val hasEnoughMoneyQuery: Query[Double *: Long *: HNil, Boolean] =
    sql"select a.deposit >= $float8 from personal_account a where a.id = $int8"
      .query(bool)

}
