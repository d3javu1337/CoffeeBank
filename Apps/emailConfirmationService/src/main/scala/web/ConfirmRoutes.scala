package web

import errors.NoSuchToken
import kafka.KafkaProducer
import mongo.EmailConfirmDocumentDAL
import zio.ZIO
import zio.http.codec.HttpCodecError.MissingQueryParam
import zio.http.{Method, Request, Response, Routes, handler}

object ConfirmRoutes {
  val routes: Routes[ConfirmRoutesEnv, Nothing] = Routes(
    Method.GET / "confirm" -> handler {
      (req: Request) => {
        ZIO.serviceWithZIO[Handler](_.confirmEmail(req.query[String]("token").toOption.get))
          .map(x => {
            if (x) Response.text("you successfully confirmed email!!!")
            else Response.text("something went wrong")
          })
          .catchAll{
            case err: MissingQueryParam => ZIO.fail(Response.badRequest(err.getMessage))
            case err: NoSuchElementException => ZIO.fail(Response.badRequest(err.getMessage))
            case err: NoSuchToken => ZIO.fail(Response.badRequest(err.getMessage))
            case err: Throwable => ZIO.fail(Response.internalServerError(err.getMessage))
          }
      }
    }
  )

  private type ConfirmRoutesEnv =
    Handler &
      EmailConfirmDocumentDAL &
      KafkaProducer

}