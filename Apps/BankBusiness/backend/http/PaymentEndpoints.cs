using System.Security.Claims;
using backend.service;
using Microsoft.AspNetCore.Mvc;

namespace backend.http;

public static class PaymentEndpoints
{
    public static void MapPaymentEndpoints(this WebApplication app)
    {
        app.MapGet("/payment", (
            ClaimsPrincipal user,
            [FromServices] PaymentService paymentService,
            [FromServices] PaymentAccountService paymentAccountService,
            [FromQuery] Guid? paymentId) =>
        {
            if (paymentId != null)
            {
                var payment = paymentService.GetPayment(GetEmail(user), paymentId.Value);
                return payment == null ? Results.NotFound() : Results.Ok(payment);
            }
            long? id = paymentAccountService.Get(GetEmail(user))?.Id;
            return id == null ? Results.NotFound() : Results.Ok(paymentService.GetAllPaymentsByPaymentAccountId(id.Value));
        }).RequireAuthorization();
        app.MapGet("/payment/check", ([FromServices] PaymentService paymentService, [FromQuery] Guid invoiceId) =>
            Results.Ok(paymentService.CheckPayment(invoiceId)))
            .RequireAuthorization();
    }

    private static string GetEmail(ClaimsPrincipal user) => user.FindFirst(ClaimTypes.Email).Value;
}