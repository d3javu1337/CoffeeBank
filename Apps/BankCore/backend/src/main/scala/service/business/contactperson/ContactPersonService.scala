package org.d3javu
package service.business.contactperson

import infra.kafka.main.dto.business.ContactPersonRequests

trait ContactPersonService[F[_]] {
  def create(dto: ContactPersonRequests.Create): F[Unit]
  def update(dto: ContactPersonRequests.Update): F[Unit]
  def delete(dto: ContactPersonRequests.Delete): F[Unit]
}
