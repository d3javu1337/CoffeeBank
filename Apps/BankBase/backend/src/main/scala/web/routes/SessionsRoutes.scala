package web.routes

import dao.mongo.SessionDocumentDAL
import model.session.Session
import security.Principal
import service.SessionService
import zio.http.Header.Authorization
import zio.{Chunk, ZIO}
import zio.http.codec.PathCodec.literal
import zio.http.{Method, Request, Response, Routes, handler, withContext}
import zio.json.*

object SessionsRoutes {

  private type SessionRoutesEnv = SessionDocumentDAL &
    SessionService &
    Principal

  val routes: Routes[SessionRoutesEnv, Nothing] = literal("sessions") / Routes.fromIterable(
    Chunk(
      Method.GET / "" -> handler {
        (req: Request) =>
          withContext((principal: Principal) => {
            ZIO.succeed(req.query[String]("sessionId").toOption)
              .flatMap {
                id =>
                  if id.isDefined
                  then ZIO.serviceWithZIO[SessionService](_.getSession(principal.email, id.get))
                  else ZIO.serviceWithZIO[SessionService](_.getAllSessions(principal.email))
              }
              .map {
                case s: Session => Response.json(s.toJson)
                case s: List[Session] => Response.json(s.toJson)
              }
              .catchAll(e => ZIO.fail(Response.badRequest))
          })
      },
      Method.DELETE / "" -> handler {
        (req: Request) =>
          withContext((principal: Principal) => {
            ZIO.succeed(req.query[String].toOption)
              .flatMap {
                id =>
                  if id.isDefined
                  then ZIO.serviceWithZIO[SessionService](_.closeSession(principal.email, id.get))
                  else ZIO.succeed(req.header(Authorization.Bearer)
                    .map(_.token.stringValue))
                    .flatMap(t => ZIO.serviceWithZIO[SessionService](_.closeSessionsExclude(principal.email, t.get)))
              }.map {
                case true => Response.ok
                case false => Response.internalServerError
              }.catchAll(e => ZIO.fail(Response.badRequest))
          })
      }
    )
  )
}
