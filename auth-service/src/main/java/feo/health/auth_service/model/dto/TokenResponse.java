package feo.health.auth_service.model.dto;

import lombok.Value;

@Value
public class TokenResponse {
    String accessToken;
    String refreshToken;
}