package web.middleware

import security.JWT.JWTService
import security.JWT.TokenType.ACCESS
import security.Principal
import zio.ZIO
import zio.http.{Handler, HandlerAspect, Header, Headers, Request, Response}

val JWTAuthMiddlewareWithContext: HandlerAspect[JWTService, Principal] =
  HandlerAspect.interceptIncomingHandler(Handler.fromFunctionZIO[Request] {
    request => request.header(Header.Authorization) match {
      case Some(Header.Authorization.Bearer(token)) =>
        ZIO.serviceWithZIO[JWTService](_.getEmail(token.stringValue, ACCESS))
          .someOrFail(Response.badRequest)
        .map(email => (request, Principal(email)))
        .catchAll(e => ZIO.fail(Response.unauthorized(e.toString)))
      case _ => ZIO.fail(Response.unauthorized.addHeaders(Headers(Header.WWWAuthenticate.Bearer(realm = "Access"))))
    }
  })