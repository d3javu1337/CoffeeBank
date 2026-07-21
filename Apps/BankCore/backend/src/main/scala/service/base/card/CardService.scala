package org.d3javu
package service.base.card

import org.d3javu.infra.kafka.main.dto.base.CardRequests.{Create, Rename}

trait CardService[F[_]] {
  def createCard(dto: Create): F[Unit]
  def renameCard(dto: Rename): F[Unit]
}
