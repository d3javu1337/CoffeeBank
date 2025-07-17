package web.routes

import dao.repository.{BusinessClientRepository, PaymentAccountRepository}
import dto.invoice.InvoiceIssueDto
import errors.NoEntityPresented
import org.d3javu.backend.grpc.invoice.ZioInvoice.InvoiceServiceClient
import security.Principal
import service.{InvoiceServiceImpl, PaymentAccountService}
import web.request.invoice.InvoiceIssueRequest
import zio.{Chunk, ZIO}
import zio.http.{Method, Request, Response, Routes, handler, withContext}
import zio.http.codec.PathCodec.literal

object InvoiceEndpoints {
  val routes: Routes[Principal & PaymentAccountRepository & BusinessClientRepository &
    InvoiceServiceImpl & InvoiceServiceClient & PaymentAccountService, Nothing] = {
    literal("invoice") / Routes.fromIterable(
      Chunk(
        Method.POST / "" -> handler {
          (req: Request) =>
            withContext((principal: Principal) => (for {
              invoiceIssueRequest <- req.body.to[InvoiceIssueRequest]
              isTokenValid <- ZIO.serviceWithZIO[PaymentAccountService](_.isTokenValid(principal.email, invoiceIssueRequest.token))
              token <- ZIO.serviceWithZIO[InvoiceServiceImpl](_.invoiceIssue(principal.email, InvoiceIssueDto(invoiceIssueRequest.amount)))
            } yield Response.text(token))
              .catchAll(e => ZIO.fail(Response.badRequest))
            )
        }
      )
    ) ++
      Routes.fromIterable(
        Chunk(
          Method.GET / "api" / "token" -> handler {
            (req: Request) =>
              withContext((principal: Principal) => (for {
                token <- ZIO.serviceWithZIO[InvoiceServiceImpl](_.getInvoiceCreateToken(principal.email))
              } yield Response.text(token.toString))
                .catchAll{
                  case r: NoEntityPresented => ZIO.fail(Response.notFound)
                  case e => ZIO.fail(Response.badRequest)
                }
              )
          },
          Method.POST / "api" / "token" -> handler {
            (req: Request) =>
              withContext((principal: Principal) => (for {
                token <- ZIO.serviceWithZIO[InvoiceServiceImpl](_.generateApiToken(principal.email))
              } yield Response.text(token))
                .catchAll(e => ZIO.fail(Response.badRequest(e.getMessage)))
              )
          }
        )
      )
  }
}
