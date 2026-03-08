package org.d3javu
package service.base.client

import domain.base.Client

import org.d3javu.domain.base.Client.{Email, PhoneNumber}
import org.d3javu.infra.kafka.main.dto.ClientRequests.RegistrationDto

trait ClientService[F[_]] {
  def registration(dto: RegistrationDto): F[Unit]
  def getClientIdByPhoneNumber(phoneNumber: PhoneNumber): F[Client.Id]
  def confirmEmail(email: Email): F[Unit]
}
