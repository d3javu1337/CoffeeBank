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
        app.MapGet("/payment", ([FromServices] PaymentService service, [FromQuery] Guid paymentId) => { });
        app.MapGet("/payment/check", () => { });
    }
}