package org.d3javu
package infra.kafka.main.dto

import domain.base.PersonalAccount.AccountId

import org.d3javu.domain.base.Client.Email

object AccountRequests {

  case class RenameRequest(
                          id: AccountId,
                          email: Email
                          )

}
