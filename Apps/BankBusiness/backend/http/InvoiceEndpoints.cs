using System.Security.Claims;

namespace backend.http;

public static class InvoiceEndpoints
{
    /*
     * 1. generate token for invoice issuing
     * 2. invoice issuing
     */
    public static void MapInvoiceEndpoints(this WebApplication app)
    {
        app.MapGet("/api/token", () =>
        {

        });
        app.MapPost("/invoice", () =>
        {

        });
    }
    
    public static string GetEmail(ClaimsPrincipal user) => user.FindFirst(ClaimTypes.Email).Value;
}