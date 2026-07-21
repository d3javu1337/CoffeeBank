package org.d3javu
package service.base.card.impl

import java.time.LocalDate
import java.util.Date

import org.d3javu.domain.Errors.CardCreateError
import org.d3javu.domain.base.Card.{CardNumber, ExpirationDate, SecurityCode}
import org.d3javu.infra.kafka.main.MainKafkaService
import org.d3javu.infra.kafka.main.dto.base.CardRequests
import org.d3javu.repository.base.card.CardRepository
import org.d3javu.service.base.personalaccount.PersonalAccountService
import org.d3javu.service.secutiry.SecurityUtilService
import org.typelevel.log4cats.Logger

import service.base.card.CardService
import cats.MonadThrow
import cats.effect.Sync
import cats.syntax.all._

class CardServiceImpl[F[_]: MonadThrow: Logger](
    securityUtilService: SecurityUtilService,
    personalAccountService: PersonalAccountService[F],
    cardRepository: CardRepository[F],
) extends CardService[F] {

  override def createCard(dto: CardRequests.Create): F[Unit] = for {
    isOwning <- personalAccountService
      .checkOwning(dto.accountId, dto.clientId, dto.email)
    _ <- MonadThrow[F].raiseWhen(isOwning)(CardCreateError)
    id <- cardRepository.initCard(
      dto.name,
      dto.cardType,
      CardNumber("-1"),
      ExpirationDate(LocalDate.now().plusYears(10)),
      dto.accountId,
      securityUtilService.generateSecurityCode,
    )
    _ <- Logger[F]
      .warn("Id of card creation exceeded 10000000. Take care about overload")
      .whenA(id.asLong > 10_000_000)
    cardNumber = securityUtilService.generateCardNumber(id, dto.cardType)
    _ <- cardRepository.updateCardAfterInit(id, cardNumber)
  } yield ()

  override def renameCard(dto: CardRequests.Rename): F[Unit] = for {
    isOwning <- personalAccountService
      .checkOwning(dto.accountId, dto.clientId, dto.email)
    _ <- MonadThrow[F].raiseWhen(isOwning)(CardCreateError)
    _ <- cardRepository.renameCard(dto.cardId, dto.newName)
  } yield ()
}

object CardServiceImpl {
  def make[F[_]: Sync: MonadThrow: Logger](
      utilServiceImpl: SecurityUtilService,
      personalAccountService: PersonalAccountService[F],
      cardRepository: CardRepository[F],
  ): CardServiceImpl[F] = new CardServiceImpl[F](
    utilServiceImpl,
    personalAccountService,
    cardRepository,
  )
}
