package org.d3javu
package service.base.client

import domain.base.Client

import org.d3javu.domain.base.Client.{Email, PhoneNumber}
import org.d3javu.infra.kafka.main.dto.base.ClientRequests.Registration

trait ClientService[F[_]] {
  def registration(dto: Registration): F[Unit]
  def getClientIdByPhoneNumber(phoneNumber: PhoneNumber): F[Client.Id]
  def confirmEmail(email: Email): F[Unit]
}
