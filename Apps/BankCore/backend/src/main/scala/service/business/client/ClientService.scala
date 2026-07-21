package org.d3javu
package service.business.client


import org.d3javu.domain.business.Client.{Email, Id}
import org.d3javu.infra.kafka.main.dto.business.ClientRequests

trait ClientService[F[_]] {
  def registration(dto: ClientRequests.Registration): F[Unit]
  def getIdByEmail(email: Email): F[Option[Id]]
}
