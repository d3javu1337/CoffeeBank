package org.d3javu
package infra.kafka.main.dto.base

import domain.base.Card.{CardId, CardName}
import domain.base.Client.Email
import domain.base.PersonalAccount.AccountId
import domain.base.{CardType, Client}

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

object CardRequests {

  case class Create(
      clientId: Client.Id,
      email: Email,
      accountId: AccountId,
      cardType: CardType,
      name: CardName,
  )

  object Create {
    implicit val createDecoder: Decoder[Create] = deriveDecoder[Create]
  }

  case class Rename(
      clientId: Client.Id,
      email: Email,
      accountId: AccountId,
      cardId: CardId,
      newName: CardName,
  )

  object Rename {
    implicit val renameDecoder: Decoder[Rename] = deriveDecoder[Rename]
  }

}
