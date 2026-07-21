package org.d3javu
package service.business

import org.d3javu.repository.business.BusinessRepositoryComponent
import org.d3javu.service.business.client.impl.ClientServiceImpl
import org.d3javu.service.business.contactperson.ContactPersonService
import org.d3javu.service.business.contactperson.impl.ContactPersonServiceImpl
import org.d3javu.service.business.invoice.InvoiceService
import org.d3javu.service.business.invoice.impl.InvoiceServiceImpl
import org.d3javu.service.business.paymentaccount.PaymentAccountService
import org.d3javu.service.business.paymentaccount.impl.PaymentAccountServiceImpl
import org.typelevel.log4cats.Logger

import service.business.client.ClientService
import cats.effect.Sync
import cats.effect.implicits.effectResourceOps
import cats.effect.kernel.Resource
import cats.syntax.all._

class BusinessServiceComponent[F[_]](
    val clientService: ClientService[F],
    val contactPersonService: ContactPersonService[F],
    val paymentAccountService: PaymentAccountService[F],
    val invoiceService: InvoiceService[F],
) {}

object BusinessServiceComponent {
  def make[F[_]: Logger: Sync](
      businessRepositoryComponent: BusinessRepositoryComponent[F],
  ): Resource[F, BusinessServiceComponent[F]] = for {
    _ <- Logger[F].info("started BusinessServiceComponent init").toResource

    clientService =
      new ClientServiceImpl[F](businessRepositoryComponent.clientRepository)
    contactPersonService = new ContactPersonServiceImpl[F](
      businessRepositoryComponent.contactPersonRepository,
      clientService,
    )
    paymentAccountService = new PaymentAccountServiceImpl[F](
      businessRepositoryComponent.paymentAccountRepository,
    )
    invoiceService = new InvoiceServiceImpl[F](
      businessRepositoryComponent.invoiceRepository,
      businessRepositoryComponent.paymentAccountRepository,
    )

    _ <- Logger[F].info("finished BusinessServiceComponent init").toResource

    comp <- Resource.Eval(Sync[F].delay(new BusinessServiceComponent[F](
      clientService,
      contactPersonService,
      paymentAccountService,
      invoiceService,
    )))

  } yield comp
}
