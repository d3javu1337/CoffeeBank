using System.Security.Claims;
using backend.dto.invoice;
using backend.http.requests;
using backend.service;
using Microsoft.AspNetCore.Mvc;

namespace backend.http;

public static class InvoiceEndpoints
{
    public static void MapInvoiceEndpoints(this WebApplication app)
    {
        app.MapPost("/api/token", ([FromServices] InvoiceService invoiceService, ClaimsPrincipal user) =>
            invoiceService.GenerateApiToken(GetEmail(user)))
            .RequireAuthorization();
        app.MapGet("/api/token", ([FromServices] InvoiceService invoiceService, ClaimsPrincipal user) =>
            invoiceService.GetInvoiceCreateToken(GetEmail(user)))
            .RequireAuthorization();
        app.MapPost("/invoice",
                ([FromServices] InvoiceService invoiceService,
                    [FromServices] PaymentAccountService paymentAccountService, ClaimsPrincipal user,
                    [FromBody] InvoiceIssueRequest dto) => 
                    !paymentAccountService.IsTokenValid(GetEmail(user), dto.token) ? 
                        Results.BadRequest() : Results.Ok(invoiceService.InvoiceIssue(GetEmail(user), new InvoiceIssueDto(dto.amount)))
                )
            .RequireAuthorization();
    }

    private static string GetEmail(ClaimsPrincipal user) => user.FindFirst(ClaimTypes.Email).Value;
}