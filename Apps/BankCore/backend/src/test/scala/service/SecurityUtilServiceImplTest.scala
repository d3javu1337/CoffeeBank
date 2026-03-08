package org.d3javu
package service

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits.catsSyntaxApplicativeId
import org.d3javu.domain.base.Card.CardId
import org.d3javu.domain.base.CardType
import org.d3javu.service.secutiry.impl.SecurityUtilServiceImpl
import org.mockito.MockitoSugar.mock
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.must.Matchers._
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.typelevel.log4cats.Logger


class SecurityUtilServiceImplTest extends AnyFlatSpec with Matchers {

  trait mocks {

    implicit val logger: Logger[IO] = mock[Logger[IO]]

    val service = new SecurityUtilServiceImpl[IO]

    val cardId = CardId(1337)

  }

  behavior of "SecurityUtilServiceImpl"

  it should "generate card number" in new mocks {

    val expectedValue = s"9999992000013377"

    val scenario = service.generateCardNumber(
      cardId,
      CardType.DEBIT
    )
    scenario shouldBe expectedValue
  }

  it should "correctly do luhn v1" in new mocks {
    val expectedValue = '8'

    val scenario = service.luhnSignature("640284501823945")

    scenario shouldBe expectedValue

  }

  it should "correctly do luhn v2" in new mocks {
    val expectedValue = '1'

    val scenario = service.luhnSignature("940211068402845")

    scenario shouldBe expectedValue

  }

}
