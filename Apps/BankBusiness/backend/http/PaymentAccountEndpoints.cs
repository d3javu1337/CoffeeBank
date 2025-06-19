using System.Security.Claims;
using backend.service;
using Microsoft.AspNetCore.Mvc;

namespace backend.http;

public static class PaymentAccountEndpoints
{
    public static void MapPaymentAccountEndpoints(this WebApplication app)
    {
        app.MapPost("/account", (ClaimsPrincipal user, [FromServices] PaymentAccountService service) =>
        {
            if(service.Get(GetEmail(user)) != null) return Results.Conflict();
            service.Create(GetEmail(user));
            return Results.Accepted();
        }).RequireAuthorization();

        app.MapGet("/account", (ClaimsPrincipal user, [FromServices] PaymentAccountService service) =>
        service.Get(GetEmail(user))).RequireAuthorization();
    }

    private static string GetEmail(ClaimsPrincipal user) => user.FindFirst(ClaimTypes.Email).Value;
}