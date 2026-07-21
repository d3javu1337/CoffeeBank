package org.d3javu
package repository.base.personalaccount

import domain.base.PersonalAccount.AccountId

import org.d3javu.domain.base.{AccountType, Client, PersonalAccount}
import org.d3javu.domain.transaction.Transaction.Money

trait PersonalAccountRepository[F[_]] {
  def checkOwning(accountId: AccountId, clientId: Client.Id, clientEmail: Client.Email): F[Boolean]
  def renameAccount(accountId: AccountId, newName: PersonalAccount.AccountName): F[Unit]
  def createAccount(clientId: Client.Id, accountType: AccountType, name: PersonalAccount.AccountName): F[Unit]
  def findAccountByClientId(clientId: Client.Id): F[AccountId]
  def hasEnoughMoney(accountId: AccountId, money: Money): F[Boolean]
}
