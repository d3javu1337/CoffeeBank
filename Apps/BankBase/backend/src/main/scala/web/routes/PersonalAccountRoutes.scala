package web.routes

import dao.postgres.repository.{ClientRepository, PersonalAccountRepository}
import internalkafka.ProducerService
import security.Principal
import service.{ClientService, PersonalAccountService}
import web.requests.personalaccount.PersonalAccountRename
import zio.http.Status.Accepted
import zio.{Chunk, ZIO}
import zio.http.codec.PathCodec.literal
import zio.http.{Method, Request, Response, Routes, handler, withContext}
import zio.json.*

object PersonalAccountRoutes {

  private type PersonalAccountRoutesEnv = Principal &
    PersonalAccountService &
    PersonalAccountRepository &
    ProducerService &
    ClientService &
    ClientRepository

  val routes: Routes[PersonalAccountRoutesEnv, Nothing] = literal("account") / Routes.fromIterable(
    Chunk(
      Method.GET / "" -> handler {
        (req: Request) =>
          withContext((principal: Principal) => {
            ZIO.serviceWithZIO[PersonalAccountService](_.getAccountByClientEmail(principal.email))
              .map(a => Response.json(a.toJson))
              .catchAll(e => ZIO.fail(Response.badRequest(e.getMessage)))
          })
      },
      Method.POST / "" -> handler {
        (req: Request) =>
          withContext((principal: Principal) => {
            ZIO.serviceWithZIO[PersonalAccountService](_.openPersonalAccount(principal.email))
              .map {
                res => if res then Response.status(Accepted) else Response.badRequest
              }
              .catchAll(e => ZIO.fail(Response.badRequest))
          })
      },
      Method.PATCH / "" -> handler {
        (req: Request) =>
          withContext((principal: Principal) => (for {
            dto <- req.body.to[PersonalAccountRename]
            res <- ZIO.serviceWithZIO[PersonalAccountService](_.renamePersonalAccount(principal.email, dto))
              .map {
                r =>
                  if r
                  then Response.status(Accepted)
                  else Response.internalServerError
              }
          } yield res)
            .catchAll(e => ZIO.fail(Response.badRequest))
          )}
    )
  )
}
