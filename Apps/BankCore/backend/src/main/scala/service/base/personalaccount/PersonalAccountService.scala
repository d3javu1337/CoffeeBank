package org.d3javu
package service.base.personalaccount

import domain.base.{Client, PersonalAccount}

trait PersonalAccountService[F[_]] {
  def createAccount: F[Unit]
  def renameAccount: F[Unit]
  def checkOwning: F[Boolean]
  def getAccountIdByClientId(clientId: Client.Id): F[PersonalAccount.AccountId]
  def hasEnoughMoney: F[Boolean]
}
