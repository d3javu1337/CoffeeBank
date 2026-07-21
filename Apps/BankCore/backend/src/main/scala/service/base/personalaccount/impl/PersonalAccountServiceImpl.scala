package org.d3javu
package service.base.personalaccount.impl

import org.d3javu.domain.base.PersonalAccount.AccountName
import org.d3javu.domain.base.{AccountType, Client, PersonalAccount}
import org.d3javu.domain.transaction.Transaction
import org.d3javu.infra.kafka.main.dto.base.AccountRequests
import org.d3javu.repository.base.personalaccount.PersonalAccountRepository

import service.base.personalaccount.PersonalAccountService
import cats.MonadThrow
import cats.effect.Sync
import cats.syntax.all._

class PersonalAccountServiceImpl[F[_]: Sync: MonadThrow](
    personalAccountRepository: PersonalAccountRepository[F],
) extends PersonalAccountService[F] {

  override def createAccount(dto: AccountRequests.Create): F[Unit] = {
    personalAccountRepository.createAccount(
      dto.id,
      AccountType.PERSONAL,
      AccountName(AccountType.PERSONAL.value),
    ).as(())
  }

  override def renameAccount(dto: AccountRequests.Rename): F[Unit] = {
    personalAccountRepository.renameAccount(dto.id, dto.newName)
  }

  override def checkOwning(
      accountId: PersonalAccount.AccountId,
      clientId: Client.Id,
      email: Client.Email,
  ): F[Boolean] = {
    personalAccountRepository.checkOwning(accountId, clientId, email)
  }

  override def getAccountIdByClientId(
      clientId: Client.Id,
  ): F[PersonalAccount.AccountId] = {
    personalAccountRepository.findAccountByClientId(clientId)
  }

  override def hasEnoughMoney(
      accountId: PersonalAccount.AccountId,
      money: Transaction.Money,
  ): F[Boolean] = {
    personalAccountRepository.hasEnoughMoney(accountId, money)
  }
}

object PersonalAccountServiceImpl {
  def make[F[_]: Sync](
      personalAccountRepository: PersonalAccountRepository[F],
  ): PersonalAccountServiceImpl[F] =
    new PersonalAccountServiceImpl[F](personalAccountRepository)
}
