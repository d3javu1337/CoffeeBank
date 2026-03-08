package org.d3javu
package service.base.client.impl

import service.base.client.ClientService

import cats.Applicative
import cats.effect.Clock
import cats.syntax.all._
import cats.implicits.toFunctorOps
import org.d3javu.domain.base.Client
import org.d3javu.infra.kafka.main.MainKafkaService
import org.d3javu.infra.kafka.main.dto.ClientRequests
import org.d3javu.infra.kafka.main.dto.ClientRequests.RegistrationDto
import org.d3javu.repository.base.client.ClientRepository
import org.typelevel.log4cats.Logger

class ClientServiceImpl[F[_]: Applicative: Logger](
                                            clientRepository: ClientRepository[F],
                                            mainKafkaService: MainKafkaService[F]
                                          ) extends ClientService[F] {

  override def registration(dto: ClientRequests.RegistrationDto): F[Unit] = {
    Logger[F].info("reg")
  }

  override def getClientIdByPhoneNumber(phoneNumber: Client.PhoneNumber): F[Client.Id] = {
    clientRepository.findClientByPhoneNumber(phoneNumber).map(_.get)
  }

  override def confirmEmail(email: Client.Email): F[Unit] = ???
}
