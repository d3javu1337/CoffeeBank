package org.d3javu
package service.base.personalaccount

import domain.base.{Client, PersonalAccount}

import org.d3javu.domain.base.Client.Email
import org.d3javu.domain.base.PersonalAccount.AccountId
import org.d3javu.domain.transaction.Transaction.Money
import org.d3javu.infra.kafka.main.dto.base.AccountRequests

trait PersonalAccountService[F[_]] {
  def createAccount(dto: AccountRequests.Create): F[Unit]
  def renameAccount(dto: AccountRequests.Rename): F[Unit]
  def checkOwning(accountId: AccountId, clientId: Client.Id, email: Email): F[Boolean]
  def getAccountIdByClientId(clientId: Client.Id): F[PersonalAccount.AccountId]
  def hasEnoughMoney(accountId: AccountId, money: Money): F[Boolean]
}
