package org.d3javu
package service.business.paymentaccount

import infra.kafka.main.dto.business.PaymentAccountRequests

trait PaymentAccountService[F[_]] {
  def createPaymentAccount(dto: PaymentAccountRequests.Create): F[Unit]
}
