package web.middleware

import zio.http.{Handler, HandlerAspect, Header, Method, Request, Response}
import zio.http.Header.AccessControlAllowMethods
import zio.http.Middleware.CorsConfig

val corsConfig: CorsConfig = CorsConfig(
  allowedOrigin = _ => Some(Header.AccessControlAllowOrigin.All),
  allowedMethods = AccessControlAllowMethods(Method.POST, Method.OPTIONS),
  allowedHeaders = Header.AccessControlAllowHeaders.All
)
