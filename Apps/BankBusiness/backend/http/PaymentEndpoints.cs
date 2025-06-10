using System.Security.Claims;
using backend.service;
using Microsoft.AspNetCore.Mvc;

namespace backend.http;

public static class PaymentEndpoints
{
    /*
     * 1. get all payments (mb pageable)
     * 2. check is invoice paid
     */
    public static void MapPaymentEndpoints(this WebApplication app)
    {
        app.MapGet("/payment", (
            ClaimsPrincipal user,
            [FromServices] PaymentService paymentService,
            [FromServices] PaymentAccountService paymentAccountService,
            [FromQuery] Guid? paymentId) =>
        {
            if (paymentId == null)
            {
                long? id = paymentAccountService.Find(GetEmail(user))?.Id;
                if (id == null) return Results.Ok();
                return Results.Ok(paymentService.GetAllByPaymentAccount(id.Value));
            }

            return Results.Ok(paymentService.GetPayment(paymentId.Value));
        })
            .RequireAuthorization();
        app.MapGet("/payment/check", ([FromServices] PaymentService paymentService, [FromQuery] Guid invoiceId) =>
            Results.Ok(paymentService.CheckPayment(invoiceId)))
            .RequireAuthorization();
    }

    public static string GetEmail(ClaimsPrincipal user) => user.FindFirst(ClaimTypes.Email).Value;
}