package org.d3javu
package repository.base.card

import org.d3javu.domain.base.CardType
import org.d3javu.domain.base.PersonalAccount.AccountId

import domain.base.Card._

trait CardRepository[F[_]] {
  def initCard(
      name: CardName,
      cardType: CardType,
      number: CardNumber,
      expirationDate: ExpirationDate,
      accountId: AccountId,
      securityCode: SecurityCode,
  ): F[CardId]

  def updateCardAfterInit(cardId: CardId, number: CardNumber): F[Unit]

  def renameCard(cardId: CardId, newName: CardName): F[Unit]
}
