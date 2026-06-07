package web.routes

import dao.repository.AdminRepositoryLive
import dto.businessclient.BusinessClientCompactWithContacts
import security.Principal
import service.AdminService
import zio.{Chunk, ZIO}
import zio.http.Routes
import zio.http.codec.PathCodec.literal
import zio.http.*
import zio.json.EncoderOps

object AdminEndpoints {
  val routes: Routes[AdminService & AdminRepositoryLive & Principal, Nothing] = literal("api") / literal("admin") / Routes.fromIterable(
    Chunk(
      Method.GET / "" -> handler {
        (req: Request) => withContext((principal: Principal) => {
          ZIO.serviceWithZIO[AdminService](_.getAllClients)
            .map(c => Response.json(c.toJson))
            .catchAll(e => ZIO.fail(Response.internalServerError(e.getMessage)))
        })
      },
      Method.GET / "client" -> handler {
        (req: Request) => withContext((principal: Principal) => (for {
          id <- req.queryZIO[Long]("clientId")
          client <- ZIO.serviceWithZIO[AdminService](_.getClientById(id))
          contacts <- ZIO.serviceWithZIO[AdminService](_.getContactsByClientId(id))
          dto = BusinessClientCompactWithContacts(
            client.id,
            client.officialName,
            client.brand,
            client.email,
            contacts
          )
        } yield Response.json(dto.toJson))
          .catchAll(e => ZIO.fail(Response.internalServerError(e.getMessage))))
      },
    )
  )
}
