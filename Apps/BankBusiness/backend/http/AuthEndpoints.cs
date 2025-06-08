using backend.dto.Auth;
using backend.dto.BusinessClient;
using backend.service;
using Microsoft.AspNetCore.Mvc;

namespace backend.http;

public static class AuthEndpoints
{
    /*
     * 1. registration
     * 2. login
     * 3. token refresh
     */
    public static void MapAuthEndpoints(this WebApplication app)
    {
        app.MapPost("/auth/registration",
            ([FromServices] BusinessClientService service, [FromBody] BusinessClientCreateDto dto) =>
            {
                return Results.Forbid();
            });
        app.MapPost("/auth/login",
            ([FromServices] BusinessClientService service, [FromBody] LoginDto dto) =>
            {
                return Results.Forbid();
            });
        app.MapPost("auth/refersh", ([FromServices] BusinessClientService service, [FromBody] JwtRequest dto) =>
        {
            return Results.Forbid();
        });
    }
}