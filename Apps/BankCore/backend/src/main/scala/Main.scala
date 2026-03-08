package org.d3javu

import cats.effect.{ExitCode, IO, IOApp, Sync}
import cats.syntax.all._
import cats.implicits.{catsSyntaxApplicativeId, catsSyntaxApplyOps}
import natchez.{Span, Trace}
import org.d3javu.domain.base.AccountType.PERSONAL
import org.d3javu.domain.base.Card.CardId
import org.d3javu.domain.base.Client.{Id, PhoneNumber}
import org.d3javu.domain.base.PersonalAccount
import org.d3javu.domain.base.PersonalAccount.{AccountDeposit, AccountId, AccountName, Cards}
import org.d3javu.domain.transaction.TransactionType
import org.d3javu.infra.{CoreLogger, Infrastructure}
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import scala.concurrent.duration.DurationInt


object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
//  override def run: IO[Unit] = {

    implicit def logger: Logger[IO] = new CoreLogger[IO].loggerForName("core")


    (for {
      _ <- Logger[IO].info("init app").toResource
      implicit0(trace: Trace[IO]) <- Trace.ioTrace(Span.noop).toResource
      app <- App.build[IO].use(c => c.pure[IO]).toResource
    } yield app).useForever
//      .use(_.use(app => {
//        app.services.base.clientService.getClientIdByPhoneNumber(PhoneNumber("80923840832"))
//          .map(_.asLong.toString)
//          .flatMap(v => Logger[IO].info(v))
//      })) *> IO(ExitCode.Success)
  }
}