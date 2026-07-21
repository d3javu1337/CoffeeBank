package org.d3javu
package service.secutiry.impl

import cats.MonadThrow
import cats.implicits._
import org.d3javu.domain.base.Card.{CardNumber, SecurityCode}
import org.d3javu.domain.base.{Card, CardType}
import org.d3javu.service.secutiry.SecurityUtilService
import org.typelevel.log4cats.Logger

import java.time.Instant
import java.util.Random

class SecurityUtilServiceImpl extends SecurityUtilService {

  private val cardNumberBase = "999999"

  private def t(cardType: CardType): String = cardType match {
          case CardType.CREDIT => "1"
          case CardType.DEBIT => "2"
          case CardType.OVERDRAFT => "3"
          case CardType.PREPAID => "4"
        }

  override def generateCardNumber(id: Card.CardId, cardType: CardType): CardNumber = {
    t(cardType).some
      .map(v => s"$cardNumberBase$v${String.format("%8s",id).replace(' ', '0')}")
      .map(v => s"$v${luhnSignature(v)}") match {
        case Some(v) => CardNumber(v)
      }
  }

  def luhnSignature(number: String): Char = {
    // (acc, pos)
    val res = number.foldRight[(Int, Int)]((0, number.length-1))((curr, t) => { (curr, t) match {
      case (c: Char, t: (Int, Int)) if t._2 % 2 === 0 => (c - '0') * 2 match {
        case c: Int if c > 9 => (t._1 + (c - 9), t._2 - 1)
        case c: Int => (t._1 + c, t._2 - 1)
      }
      case (c: Char, t: (Int, Int)) => (t._1 + (c - '0'), t._2 - 1)
    }
    })._1
    ('0' + ((10 - res%10)%10)).toChar
  }

  override def generateSecurityCode: SecurityCode = {
    val t = String.valueOf(new Random(Instant.now().toEpochMilli).nextLong(0, 1000))
    SecurityCode(t.length match {
      case 1 => s"00$t"
      case 2 => s"0$t"
      case 3 => t
    })
  }
}

