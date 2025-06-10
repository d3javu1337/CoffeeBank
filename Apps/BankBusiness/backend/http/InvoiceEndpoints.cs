using System.Security.Claims;
using backend.dto.invoice;
using backend.http.requests;
using backend.service;
using Microsoft.AspNetCore.Mvc;

namespace backend.http;

public static class InvoiceEndpoints
{
    /*
     * 1. generate token for invoice issuing
     * 2. invoice issuing
     */
    public static void MapInvoiceEndpoints(this WebApplication app)
    {
        app.MapPost("/api/token",
                ([FromServices] service.InvoiceService invoiceService, ClaimsPrincipal user) =>
                {
                    return Results.Ok(invoiceService.GenerateApiToken(GetEmail(user)));
                })
            .RequireAuthorization();
        app.MapGet("/api/token", ([FromServices] service.InvoiceService invoiceService, ClaimsPrincipal user) =>
            invoiceService.GetInvoiceCreateToken(GetEmail(user)))
            .RequireAuthorization();
        app.MapPost("/invoice",
                ([FromServices] service.InvoiceService invoiceService, ClaimsPrincipal user,
                    [FromBody] requests.InvoiceIssueRequest dto) =>
                {
                    return Results.Ok(invoiceService.InvoiceIssue(GetEmail(user), new InvoiceIssueDto(dto.amount)));
                })
            .RequireAuthorization();
    }

    public static string GetEmail(ClaimsPrincipal user) => user.FindFirst(ClaimTypes.Email).Value;
}