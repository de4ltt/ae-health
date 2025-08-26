package feo.health.auth_service.service.auth;

import feo.health.auth_service.model.dto.SignInRequest;
import feo.health.auth_service.model.dto.SignUpRequest;
import feo.health.auth_service.model.dto.TokenResponse;

public interface AuthService {
    void signUp(SignUpRequest request);
    TokenResponse signIn(SignInRequest request);
    TokenResponse refresh(String refreshToken);
}
