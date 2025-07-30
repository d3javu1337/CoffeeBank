package web.routes

import dao.mongo.SessionDocumentDAL
import dao.postgres.repository.ClientRepository
import errors.AuthError
import internalkafka.ProducerService
import security.JWT.JWTService
import security.{Principal, SecurityService}
import service.{AuthService, ClientService, SessionService}
import web.requests.{ClientRegistration, Login}
import zio.http.Header.{Authorization, UserAgent}
import zio.{Chunk, ZIO}
import zio.http.Status.Accepted
import zio.http.{Cookie, Header, Method, Path, Request, Response, Routes, handler, withContext}
import zio.http.codec.PathCodec.literal

object AuthRoutes {

  private type AuthRoutesEnv = AuthService &
    ProducerService &
    ClientRepository &
    ClientService &
    SecurityService &
    JWTService &
    SessionService &
    SessionDocumentDAL &
    ClientService &
    ClientRepository

  val routes: Routes[AuthRoutesEnv, Nothing] = literal("auth") / Routes.fromIterable(
    Chunk(
      Method.POST / "registration" -> handler {
        (req: Request) => {
          (for {
            dto <- req.body.to[ClientRegistration]
            t <- ZIO.serviceWithZIO[AuthService](_.registration(dto))
            res <- if !t then ZIO.fail(Response.internalServerError) else ZIO.succeed(Response.status(Accepted))
          } yield res)
            .catchAll(e => ZIO.fail(Response.badRequest))
        }
      },
      Method.POST / "login" -> handler {
        (req: Request) => {
          (for {
            dto <- req.body.to[Login]
            userAgentOpt <- ZIO.succeed(req.header(UserAgent))
            userAgent <- ZIO.succeed(userAgentOpt.map(u => s"${u.product.name} ${u.product.version.getOrElse("")}".strip).getOrElse("Undefined"))
            tokens <- ZIO.serviceWithZIO[AuthService](_.login(dto, userAgent))
          } yield Response.text(tokens.accessToken).addCookie(createRefreshCookieByToken(tokens.refreshToken)))
            .catchAll{
              case e: AuthError => ZIO.fail(Response.unauthorized(e.getMessage))
              case e: Throwable => ZIO.fail(Response.internalServerError)
            }
        }
      },
      Method.GET / "refresh" -> handler {
        (req: Request) => {
          (for {
            cookie <- ZIO.succeed(req.cookie("refreshToken")).someOrFail(ZIO.fail(AuthError("")))
            token <- ZIO.succeed(cookie.content)
            tokens <- ZIO.serviceWithZIO[AuthService](_.refresh(token))
          } yield Response.text(tokens.accessToken).addCookie(createRefreshCookieByToken(tokens.refreshToken)))
            .catchAll {
              case e: AuthError => ZIO.fail(Response.unauthorized(e.getMessage))
              case e: Throwable => ZIO.fail(Response.internalServerError)
            }
        }
      },
      Method.GET / "logout" -> handler {
        (req: Request) => {
          (req.header(Authorization) match {
            case Some(Header.Authorization.Bearer(token)) =>
              ZIO.serviceWithZIO[AuthService](_.logout(token.stringValue))
                .map {
                  case false => Response.internalServerError
                  case true => Response.ok
                }
            case _ => ZIO.fail(Response.unauthorized)
          })
            .catchAll(e => ZIO.fail(Response.badRequest))
        }
      },
      Method.POST / "restore-password" -> handler {
        (req: Request) => Response.notImplemented
      }
    )
  )

  private def createRefreshCookieByToken(token: String) = Cookie.Response(
    name = "refreshToken",
    content = token,
    isHttpOnly = true,
    maxAge = Option(zio.Duration.fromSeconds(30 * 24 * 60 * 60)),
    path = Option(Path("***/***"))
  )
}
