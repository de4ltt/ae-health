package feo.health.auth_service.model.token;

import java.time.Instant;

public record ParsedToken(Long userId, String email, String jti, Instant expiresAt) {}
