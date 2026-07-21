package org.d3javu
package repository.business.paymentaccount.impl

import java.util.UUID
import org.d3javu.domain.business.{Client, PaymentAccount}
import org.d3javu.repository.business.paymentaccount.PaymentAccountRepository
import org.d3javu.repository.business.paymentaccount.impl.PaymentAccountRepositoryImpl.{createInvoiceIssuingTokenCommand, createPaymentAccountCommand, existsPaymentAccountByClientIdQuery}
import cats.effect.Sync
import skunk.codec.all._
import skunk.syntax.all._
import cats.syntax.all._
import shapeless.HNil
import skunk.implicits.toStringOps
import skunk.{*:, Command, Query, Session}

class PaymentAccountRepositoryImpl[F[_]: Sync](session: Session[F])
    extends PaymentAccountRepository[F] {
  override def createPaymentAccount(
      businessClientId: Client.Id,
  ): F[PaymentAccount.Id] = (for {
    command <- session.prepare(createPaymentAccountCommand)
    id <- command.unique(businessClientId.asLong)
  } yield PaymentAccount.Id(id)).adaptError { case err => repository.DBError(err) }

  override def existsPaymentAccountByClientId(clientId: Client.Id): F[Boolean] =
    (for {
      query <- session.prepare(existsPaymentAccountByClientIdQuery)
      res <- query.unique(clientId.asLong)
    } yield res).adaptError { case err => repository.DBError(err) }

  override def createInvoiceIssuingToken(
      paymentAccountId: PaymentAccount.Id,
      token: PaymentAccount.InvoiceIssueToken,
  ): F[Unit] = (for {
    command <- session.prepare(createInvoiceIssuingTokenCommand)
    _ <- command.execute(token.asUuid :: paymentAccountId.asLong :: HNil)
  } yield ()).adaptError { case err => repository.DBError(err) }
}

object PaymentAccountRepositoryImpl {

  def make[F[_]: Sync](session: Session[F]): PaymentAccountRepositoryImpl[F] =
    new PaymentAccountRepositoryImpl[F](session)

  val createPaymentAccountCommand: Query[Long, Long] =
    sql"""insert into payment_account(name, deposit, business_client_id)
          values('Расчётный счёт', 0.0, $int8)
          returning id
       """.query(int8)

  val existsPaymentAccountByClientIdQuery: Query[Long, Boolean] =
    sql"select count(*)=1 from payment_account p where p.business_client_id= $int8"
      .query(bool)

  val createInvoiceIssuingTokenCommand: Command[UUID *: Long *: HNil] =
    sql"update payment_account set invoice_create_token= $uuid where id= $int8"
      .command

}
