using System.Security.Claims;

namespace backend.http;

public static class ContactPersonEndpoints
{
    /*
     * 1. get
     * 2. set
     */
    public static void MapContactPersonEndpoints(this WebApplication app)
    {
        
    }
    
    public static string GetEmail(ClaimsPrincipal user) => user.FindFirst(ClaimTypes.Email).Value;
}