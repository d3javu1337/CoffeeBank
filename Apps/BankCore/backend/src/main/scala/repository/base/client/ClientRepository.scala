package org.d3javu
package repository.base.client

import org.d3javu.domain.base.Client
import org.d3javu.domain.base.Client.{Email, PhoneNumber}
import org.d3javu.infra.kafka.main.dto.ClientRequests.RegistrationDto

import java.time.LocalDate

trait ClientRepository[F[_]] {
  def registration(registrationDto: RegistrationDto): F[Unit]
  def confirmEmail(email: Email): F[Unit]
  def findClientByPhoneNumber(phoneNumber: PhoneNumber): F[Option[Client.Id]]
}
