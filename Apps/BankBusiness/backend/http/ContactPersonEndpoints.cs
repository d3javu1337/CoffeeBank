using System.Security.Claims;
using backend.dto.ContactPerson;
using backend.service;
using Microsoft.AspNetCore.Mvc;

namespace backend.http;

public static class ContactPersonEndpoints
{
    public static void MapContactPersonEndpoints(this WebApplication app)
    {
        app.MapGet("/contact-person", ([FromServices] ContactPersonService contactPersonService, ClaimsPrincipal user, [FromQuery] long? personId) =>
        {
            if(personId == null) return Results.Ok(contactPersonService.GetContactPersons(GetEmail(user)));
            return Results.Ok(contactPersonService.GetContactPersonById(personId.Value));
        }).RequireAuthorization();
        app.MapPost("/contact-person", ([FromServices] ContactPersonService contactPersonService,
            ClaimsPrincipal user, [FromBody] ContactPersonCreateDto dto) =>
        {
            contactPersonService.CreateContactPerson(GetEmail(user), dto);
            return Results.Accepted();
        }).RequireAuthorization();
    }
    
    private static string GetEmail(ClaimsPrincipal user) => user.FindFirst(ClaimTypes.Email).Value;
}