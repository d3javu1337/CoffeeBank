package web

import zio.ZIO
import zio.http.Status.NotFound
import zio.http.{Method, Request, Response, Root, Routes, Status, handler}

object Routing {
  val routes: Routes[ApiHandler, Nothing] = Routes(
    Method.ANY / Root -> handler(Response.status(NotFound)),
    Method.GET / "confirm" -> handler {
      (req: Request) => {
        ZIO.serviceWithZIO[ApiHandler](_.confirmEmail(req.query[String]("token").toOption.get))
          .map(x => {
            if (x != null) Response.text("you successfully confirmed email!!!")
            else Response.text("something went wrong")
          })
          .tapError { err => zio.Console.printLine(err.toString) }
          .catchAll { err => ZIO.succeed(Response.text("some error")) }
      }
    }
  )
}