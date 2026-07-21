package org.d3javu
package repository.business.client

import domain.base.Client

import org.d3javu.domain.business.Client.{Brand, Email, Id, OfficialName, PasswordHash}

trait ClientRepository[F[_]] {
  def registration(officialName: OfficialName, brand: Brand, email: Email, passwordHash: PasswordHash): F[Id]
  def findIdByEmail(email: Email): F[Option[Id]]
}
