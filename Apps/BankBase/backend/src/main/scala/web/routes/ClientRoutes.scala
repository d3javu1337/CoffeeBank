package web.routes

import dao.postgres.repository.ClientRepository
import security.Principal
import service.ClientService
import zio.{Chunk, ZIO}
import zio.http.{Method, Request, Response, Routes, handler, withContext}
import zio.http.codec.PathCodec.literal
import zio.json._

object ClientRoutes {

  private type ClientRoutesEnv = Principal &
    ClientService &
    ClientRepository

  val routes: Routes[ClientRoutesEnv, Nothing] = literal("client") / Routes.fromIterable(
    Chunk(
      Method.GET / "" -> handler {
        (req: Request) =>
          withContext((principal: Principal) => {
            ZIO.serviceWithZIO[ClientService](_.getDtoByEmail(principal.email))
              .map(c => Response.json(c.toJson))
          }.catchAll(e => ZIO.fail(Response.badRequest(e.getMessage))))
      }
    )
  )
}
