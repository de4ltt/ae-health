package feo.health.auth_service.controller;

import feo.health.auth_service.model.dto.RefreshRequest;
import feo.health.auth_service.model.dto.SignInRequest;
import feo.health.auth_service.model.dto.SignUpRequest;
import feo.health.auth_service.model.dto.TokenResponse;
import feo.health.auth_service.service.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    CompletableFuture<ResponseEntity<Void>> signUp(@RequestBody SignUpRequest request) {
        return authService.signUp(request).thenApply(v -> ResponseEntity.ok().build());
    }

    @PostMapping("/sign-in")
    CompletableFuture<ResponseEntity<TokenResponse>> signIn(@RequestBody SignInRequest request) {
        return authService.signIn(request).thenApply(ResponseEntity::ok);
    }

    @PostMapping("/refresh")
    CompletableFuture<ResponseEntity<TokenResponse>> refresh(@RequestBody RefreshRequest request) {
        return authService.refresh(request.getRefreshToken()).thenApply(ResponseEntity::ok);
    }
}

