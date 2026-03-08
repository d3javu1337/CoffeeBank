package org.d3javu
package service.transaction

trait TransactionService[F[_]] {
  def transferByPhoneNumber: F[Unit]
  def invoicePayment: F[Unit]
}
