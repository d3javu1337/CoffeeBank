using System.Security.Claims;
using backend.service;
using Microsoft.AspNetCore.Mvc;

namespace backend.http;

public static class PaymentAccountEndpoints
{
    /*
     * 1. open payment account
     * 2. get payment account
     */
    public static void MapPaymentAccountEndpoints(this WebApplication app)
    {
        app.MapPost("/account", (ClaimsPrincipal user, [FromServices] PaymentAccountService service) =>
        {
            if(service.Find(GetEmail(user)) != null) return Results.Conflict();
            service.Create(GetEmail(user));
            return Results.Accepted();
        })
            .RequireAuthorization();
        
        app.MapGet("/account", (ClaimsPrincipal user, [FromServices] PaymentAccountService service) => 
            service.Find(GetEmail(user)))
            .RequireAuthorization();
    }
    public static string GetEmail(ClaimsPrincipal user) => user.FindFirst(ClaimTypes.Email).Value;
}