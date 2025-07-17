package web.routes

import dao.repository.BusinessClientRepository
import errors.AuthError
import io.jsonwebtoken.JwtException
import kafka.ProducerService
import org.apache.kafka.common.KafkaException
import service.{AuthService, BusinessClientService}
import web.request.auth.{BusinessClientRegistration, Login}
import zio.{Chunk, Scope, ZIO}
import zio.http.Status.Accepted
import zio.http.{Cookie, Method, Path, Request, Response, Routes, handler}
import zio.http.codec.PathCodec.literal
import web.request.auth.BusinessClientRegistration.jsonBinaryCodec
import zio.schema.codec.DecodeError
import zio.schema.codec.DecodeError.ReadError

object AuthEndpoints {
  val routes: Routes[AuthService & BusinessClientRepository & ProducerService, Nothing] = literal("auth") / Routes.fromIterable(
    Chunk(
      Method.POST / "registration" -> handler {
        (req: Request) => {
          (for {
            registrationRequest <- req.body.to[BusinessClientRegistration].catchAll(_ => ZIO.fail(Response.badRequest("Wrong request body")))
            _ <- ZIO.serviceWithZIO[AuthService](_.registration(registrationRequest))
          } yield Response.status(Accepted))
            .catchAll{
              case err: KafkaException => ZIO.fail(Response.internalServerError)
              case err: Throwable => ZIO.fail(Response.badRequest(err.getMessage))
            }
        }
      },
      Method.POST / "login" -> handler {
        (req: Request) => {
          (for {
            loginRequest <- req.body.to[Login]
            tokens <- ZIO.serviceWithZIO[AuthService](_.login(loginRequest))
          } yield Response.text(tokens.accessToken).addCookie(createRefreshCookieByToken(tokens.refreshToken)))
            .catchAll {
              case _: AuthError => ZIO.fail(Response.badRequest("Bad credentials"))
              case readError: ReadError => ZIO.fail(Response.badRequest(s"Wrong request body: ${readError.getMessage}"))
              case e: Throwable => ZIO.fail(Response.badRequest(e.getMessage))
            }
        }
      },
      Method.GET / "refresh" -> handler {
        (req: Request) => {
          (for {
            refreshCookie <- ZIO.succeed(req.cookie("refreshToken"))
            refreshToken <- ZIO.from(refreshCookie.get)
            tokens <- ZIO.serviceWithZIO[AuthService](_.refresh(refreshToken.content))
          } yield Response.text(tokens.accessToken).addCookie(createRefreshCookieByToken(tokens.refreshToken)))
            .catchAll {
              case jwtError: JwtException => ZIO.fail(Response.unauthorized(jwtError.getMessage))
              case cookieError: NoSuchElementException => ZIO.fail(Response.unauthorized("No refresh cookie present"))
              case err => ZIO.fail(Response.badRequest(err.getMessage))
            }
        }
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
