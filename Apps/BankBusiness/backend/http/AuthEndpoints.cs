using System.IdentityModel.Tokens.Jwt;
using System.Net;
using System.Security.Claims;
using System.Text.RegularExpressions;
using backend.dto.Auth;
using backend.dto.BusinessClient;
using backend.service;
using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;
using Microsoft.Net.Http.Headers;

namespace backend.http;

public static class AuthEndpoints
{
    private static readonly Regex emailRegex = new(@"^\S+@{1}[a-z]*\.{1}[a-z]+$");

    public static void MapAuthEndpoints(this WebApplication app)
    {
        app.MapPost("/auth/registration",
            ([FromServices] BusinessClientService service, [FromBody] BusinessClientCreateDto dto) =>
            {
                service.Registration(dto);
                return Results.Accepted();
            }).AllowAnonymous();
        app.MapPost("/auth/login",
            ([FromServices] AuthService service, [FromBody] LoginDto dto, HttpResponse res) =>
            {
                if (dto.Email == null || dto.Password == null || !emailRegex.IsMatch(dto.Email))
                    return Results.BadRequest();

                var tokens = service.login(dto);
                if (tokens == null) return Results.Unauthorized();
                SetRefreshTokenCookie(ref res, tokens.RefreshToken);
                return Results.Ok(tokens.AccessToken);

            }).AllowAnonymous();
        app.MapGet("auth/refresh",
            ([FromServices] AuthService service, HttpRequest req, HttpResponse res) =>
            {
                var refreshToken = req.Cookies["refreshToken"];
                if (refreshToken == null) return Results.Unauthorized();
                var tokens = service.refresh(refreshToken);
                if (tokens == null) return Results.Unauthorized();
                SetRefreshTokenCookie(ref res, tokens.RefreshToken);
                return Results.Ok(tokens.AccessToken);
            }).AllowAnonymous();
    }

    private static void SetRefreshTokenCookie(ref HttpResponse res, string refreshToken)
    {
        res.Cookies.Append(
            "refreshToken",
            refreshToken,
            new CookieOptions
            {
                HttpOnly = true,
                Expires = DateTimeOffset.UtcNow.AddDays(30),
                Path = "/"
            }
        );
    }
}