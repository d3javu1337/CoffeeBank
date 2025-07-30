package web.middleware

import dao.mongo.SessionDocumentDAL
import dao.postgres.repository.ClientRepository
import security.JWT.JWTService
import security.JWT.TokenType.ACCESS
import security.Principal
import service.{ClientService, SessionService}
import zio.ZIO
import zio.http.{Handler, HandlerAspect, Header, Headers, Request, Response}

val JWTAuthMiddlewareWithContext: HandlerAspect[JWTService & ClientService & ClientRepository & SessionService & SessionDocumentDAL, Principal] =
  HandlerAspect.interceptIncomingHandler(Handler.fromFunctionZIO[Request] {
    request =>
      request.header(Header.Authorization) match {
        case Some(Header.Authorization.Bearer(token)) =>
          (ZIO.serviceWithZIO[JWTService](_.getEmail(token.stringValue, ACCESS))
            <* ZIO.serviceWithZIO[SessionService](_.updateSessionOnInteraction(token.stringValue)))
            .flatMap(email => ZIO.serviceWithZIO[ClientService](_.loadPrincipal(email)))
            .map((request, _))
            .catchAll(e => ZIO.fail(Response.unauthorized(e.getMessage)))
        case _ => ZIO.fail(Response.unauthorized.addHeaders(Headers(Header.WWWAuthenticate.Bearer(realm = "Access"))))
      }
  })