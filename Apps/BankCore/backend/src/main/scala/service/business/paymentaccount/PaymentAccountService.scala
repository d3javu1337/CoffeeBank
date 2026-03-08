package org.d3javu
package service.business.paymentaccount

trait PaymentAccountService[F[_]] {
  def createPaymentAccount: F[Unit]
}
