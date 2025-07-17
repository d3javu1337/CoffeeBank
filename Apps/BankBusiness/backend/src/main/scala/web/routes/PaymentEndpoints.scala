package web.routes

import dao.repository.BusinessClientRepository
import security.Principal
import service.PaymentService
import zio.{Chunk, ZIO}
import zio.http.{Method, Request, Response, Routes, handler, withContext}
import zio.http.codec.PathCodec.literal
import zio.json.*
import dto.payment.PaymentWithAmountDto.*

import java.util.UUID

object PaymentEndpoints {
  val routes: Routes[Principal & BusinessClientRepository & PaymentService, Nothing] = literal("payment") / Routes.fromIterable(
    Chunk(
      Method.GET / "" -> handler {
        (req: Request) =>
          withContext((principal: Principal) => {
            val id = req.query[UUID]("paymentId").toOption
            (if id.isDefined then ZIO.serviceWithZIO[PaymentService](_.getPaymentByClientEmailAndId(principal.email, id.get))
              .map(p => Response.json(p.toJson))
            else ZIO.serviceWithZIO[PaymentService](_.getAllPaymentsByBusinessClientEmail(principal.email))
              .map(p => Response.json(p.toJson)))
              .catchAll(e => ZIO.fail(Response.badRequest))
          })
      },
      Method.GET / "check" -> handler {
        (req: Request) => withContext((principal: Principal) =>
          val id = req.query[UUID]("paymentId").toOption
          ZIO.serviceWithZIO[PaymentService](_.checkPayment(id.get))
            .map(c => Response.text(c.toString))
            .catchAll(e => ZIO.fail(Response.badRequest))
        )}
    )
  )
}
