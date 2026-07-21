package org.d3javu
package service.business.contactperson.impl

import org.d3javu.infra.kafka.main.dto.business.ContactPersonRequests
import org.d3javu.repository.business.contactperson.ContactPersonRepository
import org.d3javu.service.business.client.ClientService
import service.business.contactperson.ContactPersonService

import cats.{ApplicativeThrow, Monad, MonadThrow}
import cats.syntax.all._

class ContactPersonServiceImpl[F[_]: MonadThrow](
    contactPersonRepository: ContactPersonRepository[F],
    clientService: ClientService[F],
) extends ContactPersonService[F] {
  override def create(dto: ContactPersonRequests.Create): F[Unit] = for {
    maybeId <- clientService.getIdByEmail(dto.businessClientEmail)
    id <- maybeId.liftTo[F](new Throwable("no id presented by email"))
    _ <- contactPersonRepository.create(
      id,
      dto.surname,
      dto.name,
      dto.patronymic,
      dto.phoneNumber,
      dto.email
    )
  } yield ()

  override def update(dto: ContactPersonRequests.Update): F[Unit] = for {
    maybeId <- clientService.getIdByEmail(dto.businessClientEmail)
    id <- maybeId.liftTo[F](new Throwable("no id presented by email"))
    _ <- contactPersonRepository.update(
      id,
      dto.contactPersonId,
      dto.surname,
      dto.name,
      dto.patronymic,
      dto.phoneNumber,
      dto.email,
    )
  } yield ()

  override def delete(dto: ContactPersonRequests.Delete): F[Unit] = for {
    maybeId <- clientService.getIdByEmail(dto.businessClientEmail)
    id <- maybeId.liftTo[F](new Throwable("no id presented by email"))
    _ <- contactPersonRepository.delete(id, dto.personId)
  } yield ()
}
