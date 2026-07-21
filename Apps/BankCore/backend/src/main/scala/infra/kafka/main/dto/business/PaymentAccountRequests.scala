package org.d3javu
package infra.kafka.main.dto.business

import domain.business.Client

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

object PaymentAccountRequests {

  case class Create(clientId: Client.Id, email: Client.Email)

  implicit val decoder: Decoder[Create] = deriveDecoder[Create]
}
