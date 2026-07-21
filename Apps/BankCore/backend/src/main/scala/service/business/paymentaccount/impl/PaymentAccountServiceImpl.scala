package org.d3javu
package service.business.paymentaccount.impl

import org.d3javu.infra.kafka.main.dto.business.PaymentAccountRequests
import org.d3javu.repository.business.paymentaccount.PaymentAccountRepository
import service.business.paymentaccount.PaymentAccountService

import cats.{Applicative, Monad}
import cats.syntax.all._

class PaymentAccountServiceImpl[F[_]: Monad](
    paymentAccountRepository: PaymentAccountRepository[F],
) extends PaymentAccountService[F] {
  override def createPaymentAccount(
      dto: PaymentAccountRequests.Create,
  ): F[Unit] = {
    paymentAccountRepository.createPaymentAccount(dto.clientId).void
  }
}
