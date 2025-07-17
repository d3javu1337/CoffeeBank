package web.routes

import dao.repository.BusinessClientRepository
import security.Principal
import service.PaymentAccountService
import zio.{Chunk, ZIO}
import zio.http.{Method, Request, Response, Routes, handler, withContext}
import zio.http.codec.PathCodec.literal
import dto.paymentaccount.PaymentAccountReadDto.*
import errors.NoEntityPresented
import kafka.ProducerService
import zio.http.Status.Accepted
import zio.json.*

object PaymentAccountEndpoints {
  val routes: Routes[Principal & BusinessClientRepository & PaymentAccountService & ProducerService, Nothing] = literal("account") / Routes.fromIterable(
    Chunk(
      Method.GET / "" -> handler {
        (req: Request) => withContext((principal: Principal) => (for {
          account <- ZIO.serviceWithZIO[PaymentAccountService](_.getPaymentAccountByClientEmail(principal.email))
        } yield Response.json(account.toJson))
          .catchAll{
            case e: NoEntityPresented => ZIO.fail(Response.notFound)
            case e => ZIO.fail(Response.badRequest)
          }
        )},
      Method.POST / "" -> handler {
        (req: Request) => withContext((principal: Principal) =>
          ZIO.serviceWithZIO[PaymentAccountService](_.createPaymentAccount(principal.email))
            .as(Response.status(Accepted))
            .catchAll(e => ZIO.fail(Response.badRequest))
        )
      }
    )
  )
}
