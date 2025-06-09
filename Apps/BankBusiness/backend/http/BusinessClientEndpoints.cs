using System.Security.Claims;
using backend.repository;
using backend.service;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace backend.http;

public static class BusinessClientEndpoints
{
    /*
     * 1. get client info 
     */
    public static void MapBusinessClientEndpoints(this WebApplication app)
    {
        app.MapGet("/client", ([FromServices]BusinessClientService service, ClaimsPrincipal user) => 
            service.GetByEmail(GetEmail(user)))
        .RequireAuthorization();
    }
    
    public static string GetEmail(ClaimsPrincipal user) => user.FindFirst(ClaimTypes.Email).Value;
}