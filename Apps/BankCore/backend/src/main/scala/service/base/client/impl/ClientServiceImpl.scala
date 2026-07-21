package org.d3javu
package service.base.client.impl

import org.d3javu.domain.base.Client
import org.d3javu.domain.base.Client.Email
import org.d3javu.infra.kafka.main.MainKafkaService
import org.d3javu.infra.kafka.main.dto.base.ClientRequests
import org.d3javu.infra.kafka.util.UtilKafkaService
import org.d3javu.repository.base.client.ClientRepository
import org.typelevel.log4cats.Logger

import service.base.client.ClientService
import cats.effect.Clock
import cats.implicits.toFunctorOps
import cats.syntax.all._
import cats.{Applicative, Monad, MonadThrow}
import ClientRequests.Registration

class ClientServiceImpl[F[_]: MonadThrow: Logger](
    clientRepository: ClientRepository[F],
    utilKafkaService: UtilKafkaService[F],
) extends ClientService[F] {

  override def registration(dto: ClientRequests.Registration): F[Unit] = for {
    _ <- clientRepository.registration(dto)
    _ <- utilKafkaService.produce(dto.email)
      .handleErrorWith(err => Logger[F].warn(err)("Error when send to kafka"))
  } yield ()

  override def getClientIdByPhoneNumber(
      phoneNumber: Client.PhoneNumber,
  ): F[Client.Id] = {
    clientRepository.findClientByPhoneNumber(phoneNumber).map(_.get)
  }

  override def confirmEmail(email: Client.Email): F[Unit] = {
    clientRepository.confirmEmail(email)
  }
}
