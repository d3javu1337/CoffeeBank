using backend.dto.Auth;
using backend.kafka;
using backend.model;
using backend.repository;

namespace backend.service;

public class AuthService
{
    private readonly SecurityService _securityService;
    private readonly JwtService _jwtService;
    private readonly BusinessClientRepository _businessClientRepository;

    public AuthService(SecurityService securityService, JwtService jwtService,
        BusinessClientRepository businessClientRepository)
    {
        _securityService = securityService;
        _jwtService = jwtService;
        _businessClientRepository = businessClientRepository;
    }

    public TokensDto? login(LoginDto dto)
    {
        BusinessClient? client = _businessClientRepository.FindByEmail(dto.Email).Result;
        if (client != null && _securityService.VerifyPassword(dto.Password, client.PasswordHash))
        {
            return new TokensDto(
                _jwtService.GenerateAccessToken(dto.Email),
                _jwtService.GenerateRefreshToken(dto.Email)
            );
        }
        return null;
    }

    public TokensDto? refresh(string refreshToken)
    {
        var email = _jwtService.ExtractEmail(refreshToken);
        if (email == null) return null;
        return new TokensDto(
            _jwtService.GenerateAccessToken(email),
            _jwtService.GenerateRefreshToken(email)
        );
    }
}