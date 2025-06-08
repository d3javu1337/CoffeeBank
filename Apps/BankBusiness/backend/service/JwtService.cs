using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using Microsoft.IdentityModel.Tokens;

namespace backend.service;

public class JwtService
{

    private readonly SymmetricSecurityKey _accessSecret;
    private readonly SymmetricSecurityKey _refreshSecret;

    public JwtService(string accessSecret, string refreshSecret)
    {
        _accessSecret = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(accessSecret));
        _refreshSecret = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(refreshSecret));
    }

    public string GenerateAccessToken(string email)
    {
        var issuedDate = DateTime.UtcNow;
        var expiredDate = issuedDate.AddMinutes(15);
        return new JwtSecurityToken(
            issuer: null,
            audience: null,
            claims: [new Claim(ClaimTypes.Email, email)],
            notBefore: issuedDate,
            expires: expiredDate,
            signingCredentials: new SigningCredentials(_accessSecret, SecurityAlgorithms.HmacSha256)
        ).ToString();
    }
    
    public string GenerateRefreshToken(string email)
    {
        var issuedDate = DateTime.UtcNow;
        var expiredDate = issuedDate.AddDays(30);
        return new JwtSecurityToken(
            issuer: null,
            audience: null,
            claims: [new Claim(ClaimTypes.Email, email)],
            notBefore: issuedDate,
            expires: expiredDate,
            signingCredentials: new SigningCredentials(_refreshSecret, SecurityAlgorithms.HmacSha256)
        ).ToString();
    }
}