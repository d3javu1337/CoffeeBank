using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using Microsoft.IdentityModel.Tokens;

namespace backend.service;

public class JwtService
{

    private readonly SymmetricSecurityKey _secret;

    public JwtService(string secret)
    {
        _secret = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(secret));
    }

    public string GenerateAccessToken(string email)
    {
        var issuedDate = DateTime.UtcNow;
        var expiredDate = issuedDate.AddMinutes(15);
        var token = new JwtSecurityToken(
            issuer: null,
            audience: null,
            claims: [new Claim(ClaimTypes.Email, email)],
            notBefore: issuedDate,
            expires: expiredDate,
            signingCredentials: new SigningCredentials(_secret, SecurityAlgorithms.HmacSha256)
        );
        return new JwtSecurityTokenHandler().WriteToken(token);
    }
    
    public string GenerateRefreshToken(string email)
    {
        var issuedDate = DateTime.UtcNow;
        var expiredDate = issuedDate.AddDays(30);
        var token =  new JwtSecurityToken(
            issuer: null,
            audience: null,
            claims: [new Claim(ClaimTypes.Email, email)],
            notBefore: issuedDate,
            expires: expiredDate,
            signingCredentials: new SigningCredentials(_secret, SecurityAlgorithms.HmacSha256)
        );
        return new JwtSecurityTokenHandler().WriteToken(token);
    }
}