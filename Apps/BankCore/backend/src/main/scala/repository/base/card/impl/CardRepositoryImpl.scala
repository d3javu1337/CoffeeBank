package org.d3javu
package repository.base.card.impl

import java.util.Date
import org.d3javu.domain.base.{Card, CardType, PersonalAccount}
import org.d3javu.repository.base.card.impl.CardRepositoryImpl.{initCardCommand, renameCardCommand, updateCardAfterInitCommand}
import repository.base.card.CardRepository

import cats.effect.Sync
import skunk.codec.all._
import skunk.implicits.toStringOps
import skunk.syntax.all._
import skunk.{*:, Command, Query, Session}
import cats.syntax.all._
import org.d3javu.domain.base.Card.CardId
import shapeless.HNil

import java.time.LocalDate

private[repository] class CardRepositoryImpl[F[_]: Sync](session: Session[F])
    extends CardRepository[F] {

  override def initCard(
      name: Card.CardName,
      cardType: CardType,
      number: Card.CardNumber,
      expirationDate: Card.ExpirationDate,
      accountId: PersonalAccount.AccountId,
      securityCode: Card.SecurityCode,
  ): F[CardId] = (for {
    command <- session.prepare(initCardCommand)
    res <- command.unique(
      name.asString ::
      cardType.value ::
      number.asString ::
      expirationDate.asDate ::
      accountId.asLong ::
      securityCode.asString ::
        HNil
    )
  } yield CardId(res)).adaptError { case err => repository.DBError(err) }

  override def updateCardAfterInit(
      cardId: Card.CardId,
      number: Card.CardNumber,
  ): F[Unit] = (for {
    command <- session.prepare(updateCardAfterInitCommand)
    _ <- command.execute(number.asString :: cardId.asLong :: HNil)
  } yield ()).adaptError { case err => repository.DBError(err) }

  override def renameCard(
      cardId: Card.CardId,
      newName: Card.CardName,
  ): F[Unit] = (for {
    command <- session.prepare(renameCardCommand)
    _ <- command.execute(newName.asString :: cardId.asLong :: HNil)
  } yield ()).adaptError { case err => repository.DBError(err) }
}

private[repository] object CardRepositoryImpl {
  def make[F[_]: Sync](session: Session[F]): CardRepositoryImpl[F] = {
    new CardRepositoryImpl[F](session)
  }

  private val initCardCommand
      : Query[String *: String *: String *: LocalDate *: Long *: String *: HNil, Long] =
    sql"""insert into card (name, type, number, expiration_date, account_id, security_code)
          values ($varchar, $varchar, $varchar, $date, $int8, $varchar)
          returning id
         """.query(int8)

  private val updateCardAfterInitCommand: Command[String *: Long *: HNil] =
    sql"""
         update card set number = $varchar where id = $int8
       """.command

  private val renameCardCommand: Command[String *: Long *: HNil] =
    sql"""
         update card set name = $varchar where id = $int8
       """.command

}
