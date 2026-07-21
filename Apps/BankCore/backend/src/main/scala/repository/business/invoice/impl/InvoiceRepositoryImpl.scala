package org.d3javu
package repository.business.invoice.impl

import java.util.UUID
import org.d3javu.domain.business.{Invoice, PaymentAccount}
import repository.business.invoice.InvoiceRepository

import cats.effect.Sync
import org.d3javu.repository.business.invoice.impl.InvoiceRepositoryImpl.{createInvoiceCommand, existsInvoiceByIdQuery, getInvoiceAmountByIdQuery, getInvoiceProviderPaymentAccountIdByIdQuery}
import skunk.codec.all._
import skunk.syntax.all._
import cats.syntax.all._
import shapeless.HNil
import skunk.implicits.toStringOps
import skunk.{*:, Command, Query, Session}

class InvoiceRepositoryImpl[F[_]: Sync](session: Session[F])
    extends InvoiceRepository[F] {

  override def existsInvoiceById(id: Invoice.Id): F[Boolean] = (for {
    query <- session.prepare(existsInvoiceByIdQuery)
    res <- query.unique(id.asUUID)
  } yield res).adaptError { case err => repository.DBError(err) }

  override def getInvoiceAmountById(id: Invoice.Id): F[Option[Invoice.Amount]] = (for {
    query <- session.prepare(getInvoiceAmountByIdQuery)
    amount <- query.option(id.asUUID)
  } yield amount.map(Invoice.Amount.apply)).adaptError { case err => repository.DBError(err) }

  override def getInvoiceProviderPaymentAccountIdById(
      id: Invoice.Id,
  ): F[Option[PaymentAccount.Id]] = (for {
    query <- session.prepare(getInvoiceProviderPaymentAccountIdByIdQuery)
    id <- query.option(id.asUUID)
  } yield id.map(PaymentAccount.Id.apply)).adaptError { case err => repository.DBError(err) }

  override def createInvoice(
      providerPaymentAccountId: PaymentAccount.Id,
      amount: Invoice.Amount,
  ): F[Invoice.Id] = (for {
    command <- session.prepare(createInvoiceCommand)
    id <- command.unique(amount.asDouble :: providerPaymentAccountId.asLong :: HNil)
  } yield Invoice.Id(id)).adaptError { case err => repository.DBError(err) }
}

object InvoiceRepositoryImpl {

  def make[F[_]: Sync](session: Session[F]): InvoiceRepositoryImpl[F] =
    new InvoiceRepositoryImpl[F](session)

  val existsInvoiceByIdQuery: Query[UUID, Boolean] =
    sql"select count(*)=1 from invoice i where i.id= $uuid".query(bool)

  val getInvoiceAmountByIdQuery: Query[UUID, Double] =
    sql"select i.amount from invoice i where i.id= $uuid".query(float8)

  val getInvoiceProviderPaymentAccountIdByIdQuery: Query[UUID, Long] =
    sql"select i.provider_payment_account_id from invoice i where i.id= $uuid"
      .query(int8)

  val createInvoiceCommand: Query[Double *: Long *: HNil, UUID] =
    sql"""insert into invoice(id, amount, provider_payment_account_id)
          values(gen_random_uuid(), $float8, $int8)
          returning id
       """.query(uuid)

}
