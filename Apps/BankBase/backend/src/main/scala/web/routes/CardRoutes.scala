package web.routes

import dao.postgres.repository.{CardRepository, ClientRepository, PersonalAccountRepository}
import internalkafka.ProducerService
import model.card.CardType
import model.card.CardType.{CREDIT, DEBIT}
import security.Principal
import service.{CardService, ClientService, PersonalAccountService}
import web.requests.card.{CardCreate, CardRename}
import zio.http.Status.Accepted
import zio.{Chunk, ZIO}
import zio.http.{Method, Request, Response, Routes, handler, withContext}
import zio.http.codec.PathCodec.literal
import zio.json.*

object CardRoutes {

  private type CardRoutesEnv = Principal &
    PersonalAccountService &
    PersonalAccountRepository &
    CardService &
    CardRepository &
    ProducerService &
    ClientService &
    ClientRepository

  val routes: Routes[CardRoutesEnv, Nothing] = literal("card") / Routes.fromIterable(
    Chunk(
      Method.GET / "" -> handler {
        (req: Request) =>
          withContext((principal: Principal) => {
            (for {
              cardId <- ZIO.succeed(req.query[Long]("cardId").toOption)
              resp <- if cardId.isDefined
              then ZIO.serviceWithZIO[CardService](_.getCardById(cardId.get)).map(_.toJson)
              else ZIO.serviceWithZIO[CardService](_.getCardsByClientEmail(principal.email)).map(_.toJson)
            } yield Response.json(resp))
              .catchAll(e => ZIO.fail(Response.badRequest))
          })
      },
      Method.POST / "" -> handler {
        (req: Request) =>
          withContext((principal: Principal) => {
            (for {
              dtoOpt <- req.body.to[CardCreate].option
              dto <- ZIO.succeed(dtoOpt.getOrElse(CardCreate(DEBIT)))
              _ <- ZIO.logInfo(dto.cardType.toString)
              resp <- ZIO.serviceWithZIO[CardService](_.openCard(principal.email, dto.cardType))
                .map(r => if r then Response.status(Accepted) else Response.badRequest)
            } yield resp)
              .catchAll(e => ZIO.fail(Response.badRequest))
          })
      },
      Method.PATCH / "" -> handler {
        (req: Request) =>
          withContext((principal: Principal) => {
            (for {
              renameRequest <- req.body.to[CardRename].option
              resp <- if renameRequest.isDefined
              then ZIO.serviceWithZIO[CardService](_.renameCard(principal.email, renameRequest.get))
                  .as(Response.status(Accepted))
              else ZIO.fail(Response.badRequest(""))
            } yield resp)
              .catchAll(e => ZIO.fail(Response.badRequest))
          })
      }
    )
  )
}
