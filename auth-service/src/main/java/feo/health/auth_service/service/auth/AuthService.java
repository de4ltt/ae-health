package feo.health.auth_service.service.auth;

import feo.health.auth_service.model.dto.SignInRequest;
import feo.health.auth_service.model.dto.SignUpRequest;
import feo.health.auth_service.model.dto.TokenResponse;

import java.util.concurrent.CompletableFuture;

public interface AuthService {
    CompletableFuture<Void> signUp(SignUpRequest request);
    CompletableFuture<TokenResponse> signIn(SignInRequest request);
    CompletableFuture<TokenResponse> refresh(String refreshToken);
}

