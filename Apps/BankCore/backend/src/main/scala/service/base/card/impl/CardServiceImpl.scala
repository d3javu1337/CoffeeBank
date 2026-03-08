package org.d3javu
package service.base.card.impl

import service.base.card.CardService

import org.d3javu.infra.kafka.main.MainKafkaService

class CardServiceImpl[F[_]](
    mainKafkaService: MainKafkaService[F]
                           ) extends CardService[F] {

  override def createCard: F[Unit] = ???

  override def renameCard: F[Unit] = ???
}
