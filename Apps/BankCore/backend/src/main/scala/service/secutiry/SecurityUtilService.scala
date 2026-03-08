package org.d3javu
package service.secutiry

import domain.base.Card.CardId

import org.d3javu.domain.base.CardType

trait SecurityUtilService {

  def generateCardNumber(id: CardId, cardType: CardType): String

  def generateSecurityCode: String

}
