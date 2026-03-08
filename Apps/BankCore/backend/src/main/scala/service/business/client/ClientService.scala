package org.d3javu
package service.business.client

import domain.base.Client

trait ClientService[F[_]] {
  def registration: F[Unit]
}
