package org.d3javu
package infra.kafka.main.dto.base

import domain.base.Client
import domain.base.Client.Email
import domain.base.PersonalAccount.{AccountId, AccountName}

import io.circe.Decoder
import io.circe.generic.auto._
import io.circe.generic.semiauto.deriveDecoder

object AccountRequests {

  case class Rename(
      clientId: Client.Id,
      email: Email,
      id: AccountId,
      newName: AccountName,
  )

  object Rename {
    implicit val renameDecoder: Decoder[Rename] = deriveDecoder[Rename]
  }

  case class Create(id: Client.Id, email: Email)

  implicit val createDecoder: Decoder[Create] = deriveDecoder[Create]

}
