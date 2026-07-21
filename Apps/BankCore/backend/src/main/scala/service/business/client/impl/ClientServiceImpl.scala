package org.d3javu
package service.business.client.impl

import service.business.client.ClientService

import cats.Monad
import org.d3javu.domain.business.Client
import org.d3javu.infra.kafka.main.dto.business.ClientRequests
import org.d3javu.repository.business.client.ClientRepository
import cats.syntax.all._

class ClientServiceImpl[F[_]: Monad](clientRepository: ClientRepository[F]) extends ClientService[F] {
  override def registration(dto: ClientRequests.Registration): F[Unit] = {
    clientRepository.registration(
      dto.officialName,
      dto.brand,
      dto.email,
      dto.passwordHash
    ).void
  }

  override def getIdByEmail(email: Client.Email): F[Option[Client.Id]] = {
    clientRepository.findIdByEmail(email)
  }
}
