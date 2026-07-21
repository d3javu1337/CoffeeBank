package org.d3javu
package repository.business

import org.d3javu.repository.business.client.ClientRepository
import org.d3javu.repository.business.client.impl.ClientRepositoryImpl
import org.d3javu.repository.business.contactperson.ContactPersonRepository
import org.d3javu.repository.business.contactperson.impl.ContactPersonRepositoryImpl
import org.d3javu.repository.business.invoice.InvoiceRepository
import org.d3javu.repository.business.invoice.impl.InvoiceRepositoryImpl
import org.d3javu.repository.business.payment.PaymentRepository
import org.d3javu.repository.business.payment.impl.PaymentRepositoryImpl
import org.d3javu.repository.business.paymentaccount.PaymentAccountRepository
import org.d3javu.repository.business.paymentaccount.impl.PaymentAccountRepositoryImpl

import cats.effect.Sync
import skunk.Session

class BusinessRepositoryComponent[F[_]](
    val clientRepository: ClientRepository[F],
    val contactPersonRepository: ContactPersonRepository[F],
    val invoiceRepository: InvoiceRepository[F],
    val paymentRepository: PaymentRepository[F],
    val paymentAccountRepository: PaymentAccountRepository[F],
) {}

object BusinessRepositoryComponent {
  def make[F[_]: Sync](session: Session[F]): BusinessRepositoryComponent[F] =
    new BusinessRepositoryComponent[F](
      ClientRepositoryImpl.make[F](session),
      ContactPersonRepositoryImpl.make[F](session),
      InvoiceRepositoryImpl.make[F](session),
      PaymentRepositoryImpl.make[F](session),
      PaymentAccountRepositoryImpl.make[F](session),
    )
}
