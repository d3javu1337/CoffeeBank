package web

import mongo.{EmailConfirmDocumentDAL, MongoBase}
import zio.ZIO
import zio.http.Status.{Forbidden, NotFound}
import zio.http.{Method, Request, Response, Root, Routes, handler}

object Routing {
  val routes: Routes[ApiHandler, Nothing] = Routes(
    Method.ANY / Root -> handler(Response.status(NotFound)),
    Method.GET / "confirm" -> handler {
      (req: Request) => {
        ZIO.serviceWithZIO[ApiHandler](_.confirmEmail(req.query[String]("token").toString))
          .map(x => Response.text(x.toString))
          .tapError{err => zio.Console.printLine(err.toString)}
          .catchAll{err => ZIO.succeed(Response.text("some error"))
          }
      }
    }
  )
}