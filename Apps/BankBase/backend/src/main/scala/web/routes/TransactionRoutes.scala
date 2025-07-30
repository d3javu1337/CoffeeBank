package web.routes

import dao.postgres.repository.{InvoiceRepository, PaymentRepository}
import errors.ValidationError
import org.d3javu.backend.grpc.transactions.ZioTransactions.TransactionServiceClient
import security.Principal
import service.{InvoiceService, PaymentService, TransactionService}
import web.requests.transactions.{PurchaseRequest, TransferByPhoneNumberRequest}
import web.responses.transaction.CompletionResponse
import zio.{Chunk, ZIO}
import zio.http.{Method, Request, Response, Routes, handler, withContext}
import zio.http.codec.PathCodec.literal
import zio.json.*

object TransactionRoutes {

  private type TransactionRoutesEnv = Principal &
    InvoiceService &
    InvoiceRepository &
    PaymentService &
    PaymentRepository &
    TransactionService &
    TransactionServiceClient

  val routes: Routes[TransactionRoutesEnv, Nothing] = literal("transaction") / Routes.fromIterable(
    Chunk(
      Method.POST / "transfer" -> handler {
        (req: Request) =>
          withContext((principal: Principal) => {
            req.body.to[TransferByPhoneNumberRequest]
              .flatMap{ r =>
                if r.phoneNumber.matches("[0-9]{10}|[0-9]{11}")
                then ZIO.succeed(r)
                else ZIO.fail(ValidationError("wrong phone number"))
              }.flatMap(request =>
                ZIO.serviceWithZIO[TransactionService](_.transferByPhoneNumber(
                  request.phoneNumber,
                  request.accountId,
                  request.amount)))
              .map(b => Response.json(CompletionResponse(b).toJson))
              .catchAll(e => ZIO.fail(Response.badRequest(e.getMessage)))
          })
      },
      Method.POST / "purchase" -> handler {
        (req: Request) => withContext((principal: Principal) => {
          req.body.to[PurchaseRequest]
            .flatMap(r => ZIO.serviceWithZIO[TransactionService](_.purchase(r.invoiceId, r.accountId)))
            .map(b => Response.json(CompletionResponse(b).toJson))
            .catchAll(e => ZIO.fail(Response.badRequest))
        })
      },
      Method.GET / "invoice-info" -> handler {
        (req: Request) =>
          withContext((principal: Principal) => {
            ZIO.succeed(req.query[String]("invoiceNumber").toOption)
              .someOrFail(Response.badRequest)
              .flatMap(number => ZIO.serviceWithZIO[InvoiceService](_.getInvoiceInfo(number)))
              .map(i => Response.json(i.toJson))
              .catchAll(e => ZIO.fail(Response.internalServerError))
          })
      }
    )
  )
}
