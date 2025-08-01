package web.routes

import dao.postgres.repository.{InvoiceRepository, PaymentRepository, PersonalAccountRepository, TransactionRepository}
import dto.transaction.TransactionReadDto
import errors.ValidationError
import org.d3javu.backend.grpc.transactions.ZioTransactions.TransactionServiceClient
import security.Principal
import service.{InvoiceService, PaymentService, PersonalAccountService, TransactionService}
import util.PageRequest
import web.requests.transactions.{PurchaseRequest, TransferByPhoneNumberRequest}
import web.responses.transaction.CompletionResponse
import zio.http.codec.HttpCodecError.MissingQueryParams
import zio.{Chunk, ZIO}
import zio.http.{Method, Request, Response, Routes, handler, withContext}
import zio.http.codec.PathCodec.literal
import zio.json.*

import java.util.UUID

object TransactionRoutes {

  private type TransactionRoutesEnv = Principal &
    InvoiceService &
    InvoiceRepository &
    PaymentService &
    PaymentRepository &
    TransactionService &
    TransactionRepository &
    TransactionServiceClient &
    PersonalAccountService &
    PersonalAccountRepository

  val routes: Routes[TransactionRoutesEnv, Nothing] = literal("transaction") / Routes.fromIterable(
    Chunk(
      Method.POST / "transfer" -> handler {
        (req: Request) =>
          withContext((principal: Principal) => {
            req.body.to[TransferByPhoneNumberRequest]
              .flatMap { r =>
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
        (req: Request) =>
          withContext((principal: Principal) => {
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
      },
      Method.GET / "" -> handler {
        (req: Request) =>
          withContext((principal: Principal) => {
            (for {
              idOpt <- ZIO.succeed(req.query[String]("transactionId").toOption)
              pageNumber <- ZIO.succeed(req.query[Int]("page").toOption)
              pageSize <- ZIO.succeed(req.query[Int]("size").toOption)
              data <- if idOpt.isDefined
              then ZIO.serviceWithZIO[TransactionService](_.getTransactionById(principal.email, idOpt.get))
                  .catchAll(e => ZIO.fail(e.getMessage))
              else ZIO.serviceWithZIO[TransactionService](_.getTransactionsPageable(principal.email, pageNumber, pageSize))
              res <- data match
                case t: TransactionReadDto => ZIO.succeed(Response.json(t.toJson))
                case t: List[TransactionReadDto] => ZIO.succeed(Response.json(t.toJson))
            } yield res)
              .catchAll(e => ZIO.fail(Response.badRequest(e.toString)))
          })
      },
    )
  )
}
