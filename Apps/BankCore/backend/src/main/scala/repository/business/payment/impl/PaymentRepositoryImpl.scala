package org.d3javu
package repository.business.payment.impl

import java.util.UUID
import org.d3javu.domain.base.PersonalAccount
import org.d3javu.domain.business.{Invoice, Payment, PaymentAccount}
import org.d3javu.domain.transaction.Transaction
import org.d3javu.repository.business.payment.impl.PaymentRepositoryImpl.{createPaymentCommand, sendMoneyToRecipientCommand, takeMoneyFromPayerCommand}
import repository.business.payment.PaymentRepository

import cats.effect.Sync
import skunk.codec.all._
import cats.syntax.all._
import shapeless.HNil
import skunk.implicits.toStringOps
import skunk.{*:, Query, Session}

class PaymentRepositoryImpl[F[_]: Sync](session: Session[F])
    extends PaymentRepository[F] {
  override def takeMoneyFromPayer(
      payerAccountId: PersonalAccount.AccountId,
      amount: Invoice.Amount,
  ): F[Int] = (for {
    command <- session.prepare(takeMoneyFromPayerCommand)
    count <- command.unique(amount.asDouble :: payerAccountId.asLong :: HNil)
  } yield count).adaptError { case err => repository.DBError(err) }

  override def sendMoneyToRecipient(
      recipientAccountId: PaymentAccount.Id,
      amount: Invoice.Amount,
  ): F[Int] = (for {
    command <- session.prepare(sendMoneyToRecipientCommand)
    count <- command.unique(amount.asDouble :: recipientAccountId.asLong :: HNil)
  } yield count).adaptError { case err => repository.DBError(err) }

  override def createPayment(
      providerPaymentAccountId: PaymentAccount.Id,
      payerPersonalAccountId: PersonalAccount.AccountId,
      transactionId: Transaction.Id,
      invoiceId: Invoice.Id,
  ): F[Payment.Id] = (for {
    command <- session.prepare(createPaymentCommand)
    id <- command.unique(
      providerPaymentAccountId.asLong ::
      payerPersonalAccountId.asLong ::
      transactionId.asUUID ::
      invoiceId.asUUID ::
        HNil
    )
  } yield Payment.Id(id)).adaptError { case err => repository.DBError(err) }
}

object PaymentRepositoryImpl {

  def make[F[_]: Sync](session: Session[F]): PaymentRepositoryImpl[F] =
    new PaymentRepositoryImpl[F](session)

  val takeMoneyFromPayerCommand: Query[Double *: Long *: HNil, Int] =
    sql"update personal_account set deposit = deposit - $float8 where id = $int8"
      .query(int4)

  val sendMoneyToRecipientCommand: Query[Double *: Long *: HNil, Int] =
    sql"update payment_account set deposit = deposit + $float8 where id = $int8"
      .query(int4)

  val createPaymentCommand: Query[Long *: Long *: UUID *: UUID *: HNil, UUID] =
    sql"""insert into payment(id, payment_account_id, personal_account_id, transaction_id, invoice_id)
          values (gen_random_uuid(), $int8, $int8, $uuid, $uuid)
          returning id
       """.query(uuid)

}
