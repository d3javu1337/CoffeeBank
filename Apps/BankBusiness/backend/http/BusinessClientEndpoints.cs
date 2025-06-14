using System.Security.Claims;
using backend.repository;
using backend.service;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace backend.http;

public static class BusinessClientEndpoints
{
    public static void MapBusinessClientEndpoints(this WebApplication app)
    {
        app.MapGet("/business-client", ([FromServices] BusinessClientService service, ClaimsPrincipal user) =>
            service.GetByEmail(GetEmail(user)))
            .RequireAuthorization();
    }

    private static string GetEmail(ClaimsPrincipal user) => user.FindFirst(ClaimTypes.Email).Value;
}