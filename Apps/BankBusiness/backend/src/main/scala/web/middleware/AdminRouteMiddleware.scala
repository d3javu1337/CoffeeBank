package web.middleware

import service.AdminService
import zio.ZIO
import zio.http.{HandlerAspect, Response}

val AdminMiddleware: HandlerAspect[AdminService, Unit] = {
  HandlerAspect.allowZIO(r =>
    r.headers.get("email") match {
      case Some(email) => ZIO.serviceWithZIO[AdminService](_.isAdmin(email)).orElseFail(Response.forbidden("no admin"))
      case None => ZIO.fail(Response.forbidden("no admin"))
    }
  )
}
