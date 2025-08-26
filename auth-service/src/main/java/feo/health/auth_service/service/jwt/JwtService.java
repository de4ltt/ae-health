package feo.health.auth_service.service.jwt;

import feo.health.auth_service.model.token.ParsedToken;

public interface JwtService {
    String generateAccessToken(Long userId, String email);
    String generateRefreshToken(Long userId, String email, String jti);
    ParsedToken parseAccess(String token);
    ParsedToken parseRefresh(String token);
}