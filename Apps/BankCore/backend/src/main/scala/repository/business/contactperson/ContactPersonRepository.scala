package org.d3javu
package repository.business.contactperson

import org.d3javu.domain.business.ContactPerson.{
  Email, Id, Name, Patronymic, PhoneNumber, Surname,
}

import domain.business.Client

trait ContactPersonRepository[F[_]] {
  def create(
      businessClientId: Client.Id,
      surname: Surname,
      name: Name,
      patronymic: Patronymic,
      phoneNumber: PhoneNumber,
      email: Email,
  ): F[Id]

  def update(
      businessClientId: Client.Id,
      id: Id,
      surname: Surname,
      name: Name,
      patronymic: Patronymic,
      phoneNumber: PhoneNumber,
      email: Email,
  ): F[Unit]

  def delete(businessClientId: Client.Id, personId: Id): F[Unit]
}
