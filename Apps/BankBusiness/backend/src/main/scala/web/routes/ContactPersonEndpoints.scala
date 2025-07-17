package web.routes

import dao.repository.BusinessClientRepository
import dto.contactperson.{ContactPersonCreateDto, ContactPersonReadDto, ContactPersonUpdateDto}
import errors.{NoEntityPresented, RequestBodyException}
import model.ContactPerson
import security.Principal
import service.{BusinessClientService, ContactPersonService}
import zio.http.Status.{Accepted, NotFound}
import zio.http.codec.HttpCodecError.MissingQueryParam
import zio.{Chunk, ZIO}
import zio.http.{Method, Request, Response, Routes, handler, withContext}
import zio.http.codec.PathCodec.literal
import zio.json.*
import dto.contactperson.ContactPersonReadDto.*
import kafka.ProducerService

object ContactPersonEndpoints {
  val routes: Routes[Principal & ContactPersonService & BusinessClientService & BusinessClientRepository & ProducerService, Nothing] = literal("contact-person") / Routes.fromIterable(
    Chunk(
      Method.GET / "" -> handler {
        (req: Request) => withContext((principal: Principal) =>  {
          val id = req.query[Long]("id").toOption
          (if id.isDefined then ZIO.serviceWithZIO[ContactPersonService](_.getContactPersonDtoByEmail(principal.email, id.get))
            .map(c => Response.json(c.toJson))
          else ZIO.serviceWithZIO[ContactPersonService](_.getContactPersons(principal.email))
            .map(c => Response.json(c.toJson)))
            .catchAll {
              case e: NoEntityPresented => ZIO.fail(Response.error(NotFound, e.getMessage))
              case e => ZIO.logError(e.getMessage) *> ZIO.fail(Response.internalServerError(e.getMessage))
            }
        })
      },
      Method.POST / "" -> handler {
        (req: Request) => withContext((principal: Principal) =>  {
          (for {
            dto <- req.body.to[ContactPersonCreateDto]
              .orElseFail(RequestBodyException())
            _ <- ZIO.serviceWithZIO[ContactPersonService](_.createContactPerson(principal.email, dto))
          } yield Response.status(Accepted))
            .catchAll {
              e => ZIO.fail(Response.badRequest(e.getMessage))
            }
          })
      },
      Method.PUT / "" -> handler {
        (req: Request) => withContext((principal: Principal) => {
          (for {
            dto <- req.body.to[ContactPersonUpdateDto]
              .orElseFail(RequestBodyException())
            _ <- ZIO.serviceWithZIO[ContactPersonService](_.updateContactPerson(principal.email, dto))
          } yield Response.status(Accepted))
            .catchAll(e => ZIO.fail(Response.badRequest(e.getMessage)))
        })
      },
      Method.DELETE / "" -> handler {
        (req: Request) => withContext((principal: Principal) => {
          (for {
            personId <- ZIO.fromEither(req.query[Long]("personId"))
            _ <- ZIO.serviceWithZIO[ContactPersonService](_.deleteContactPerson(principal.email, personId))
          } yield Response.status(Accepted))
            .catchAll(e => ZIO.fail(Response.badRequest))
        })
      }
    )
  )
}
