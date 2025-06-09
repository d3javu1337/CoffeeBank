using System.Net;
using System.Text.RegularExpressions;
using backend.dto.Auth;
using backend.dto.BusinessClient;
using backend.service;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Net.Http.Headers;

namespace backend.http;

public static class AuthEndpoints
{
    /*
     * 1. registration
     * 2. login
     * 3. token refresh
     */

    private static readonly Regex emailRegex = new(@"^\S+@{1}[a-z]*\.{1}[a-z]+$");

    public static void MapAuthEndpoints(this WebApplication app)
    {
        app.MapPost("/auth/registration",
            ([FromServices] BusinessClientService service, [FromBody] BusinessClientCreateDto dto) =>
            {
                service.Registration(dto);
                return Results.Accepted();
            });
        app.MapPost("/auth/login",
            ([FromServices] AuthService service, [FromBody] LoginDto dto, HttpRequest req, HttpResponse res) =>
            {
                if (dto.email == null || dto.password == null || !emailRegex.IsMatch(dto.email))
                {
                    return Results.BadRequest();
                }
                var tokens = service.login(dto);
                {
                    if (tokens != null)
                    {
                        SetRefreshTokenCookie(ref res, tokens.RefreshToken);
                        return Results.Ok(tokens.AccessToken);
                    }
                    return Results.Unauthorized();
                }
            });
        app.MapPost("auth/refersh",
            ([FromServices] BusinessClientService service, [FromBody] JwtRequest dto) =>
            {
                return Results.Forbid();
            });
    }

    private static void SetRefreshTokenCookie(ref HttpResponse res, string refreshToken)
    {
        res.Cookies.Append(
            "refreshToken",
            refreshToken,
            new CookieOptions()
            {
                HttpOnly = true,
                Expires = DateTimeOffset.UtcNow.AddDays(30),
                Path = "***/***"
            }
            );
    }
}