package web.routes

import dao.repository.BusinessClientRepositoryLive
import dto.businessclient.BusinessClientReadDto
import security.Principal
import service.BusinessClientService
import zio.http.codec.HttpCodecError.MissingQueryParam
import zio.{Chunk, ZIO}
import zio.http.{Method, Request, Response, Routes, handler, withContext}
import zio.http.codec.PathCodec.literal
import zio.json._

object BusinessClientEndpoints {
  val routes: Routes[Principal & BusinessClientService & BusinessClientRepositoryLive, Nothing] = literal("business-client") / Routes.fromIterable(
    Chunk(
      Method.GET / "" -> handler {
        (req: Request) =>
          withContext((principal: Principal) => {
            (for {
              client <- ZIO.serviceWithZIO[BusinessClientService](_.getDtoByEmail(principal.email))
            } yield Response.json(client.toJson))
              .catchAll {
                case e: MissingQueryParam => ZIO.fail(Response.badRequest(e.getMessage))
                case e: Throwable => ZIO.fail(Response.badRequest(e.getMessage))
              }
          })
      }
    )
  )
}
