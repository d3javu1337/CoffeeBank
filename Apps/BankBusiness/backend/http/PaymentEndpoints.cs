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
            [FromQuery] Guid paymentId) =>
        {
            if (paymentId == Guid.Empty)
            {
                return Results.Ok(paymentService.GetAllByPaymentAccount(paymentAccountService.Find(GetEmail(user)).Id));
            }

            return Results.Ok(paymentService.GetPayment(paymentId));
        });
        app.MapGet("/payment/check", ([FromServices] PaymentService paymentService, [FromQuery] Guid invoiceId) =>
            Results.Ok(paymentService.CheckPayment(invoiceId)));
    }

    public static string GetEmail(ClaimsPrincipal user) => user.FindFirst(ClaimTypes.Email).Value;
}