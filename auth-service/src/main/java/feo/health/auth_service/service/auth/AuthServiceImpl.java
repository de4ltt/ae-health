package feo.health.auth_service.service.auth;

import auth.Auth;
import auth.AuthServiceGrpc;
import com.google.protobuf.Empty;
import feo.health.auth_service.model.dto.SignInRequest;
import feo.health.auth_service.model.dto.SignUpRequest;
import feo.health.auth_service.model.dto.TokenResponse;
import feo.health.auth_service.model.entity.RefreshToken;
import feo.health.auth_service.model.entity.UserCredentials;
import feo.health.auth_service.model.token.ParsedToken;
import feo.health.auth_service.repository.RefreshTokenRepository;
import feo.health.auth_service.repository.UserCredentialsRepository;
import feo.health.auth_service.service.jwt.JwtService;
import feo.health.auth_service.service.user.UserService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

// AuthServiceImpl.java
@GrpcService
@RequiredArgsConstructor
public class AuthServiceImpl extends AuthServiceGrpc.AuthServiceImplBase implements AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserCredentialsRepository userCredentialsRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void changeUserPassword(Auth.ChangeUserPasswordRequest request, StreamObserver<Empty> responseObserver) {
        // This path is gRPC-internal. Keep it fast, wrap blocking in virtual thread via @Transactional + VT.
        UserCredentials creds = userCredentialsRepository.findById(request.getUserId())
                .orElseThrow(jakarta.persistence.EntityNotFoundException::new);
        if (!passwordEncoder.matches(request.getOldPassword(), creds.getPasswordEncoded()))
            throw new RuntimeException("Wrong password.");
        creds.setPasswordEncoded(passwordEncoder.encode(request.getNewPassword()));
        userCredentialsRepository.save(creds);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Async
    @Transactional
    @Override
    public CompletableFuture<Void> signUp(SignUpRequest request) {
        return userService.getUserIdByEmailIfExists(request.getEmail())
                .thenCompose(opt -> {
                    if (opt.isPresent()) {
                        CompletableFuture<Void> f = new CompletableFuture<>();
                        f.completeExceptionally(new RuntimeException("User already exists."));
                        return f;
                    }
                    return userService.saveUser(request);
                });
    }

    @Async
    @Override
    public CompletableFuture<TokenResponse> signIn(SignInRequest request) {
        return userService.getUserIdByEmailIfExists(request.getEmail())
                .thenCompose(opt -> {
                    if (opt.isEmpty()) {
                        CompletableFuture<TokenResponse> f = new CompletableFuture<>();
                        f.completeExceptionally(new RuntimeException("User does not exist."));
                        return f;
                    }
                    Long userId = opt.get();
                    return CompletableFuture.supplyAsync(() ->
                            userCredentialsRepository.findById(userId)
                                    .orElseThrow(() -> new RuntimeException("User does not exist."))
                    ).thenApply(creds -> {
                        if (passwordEncoder.matches(request.getPassword(), creds.getPasswordEncoded()))
                            return issueTokens(userId, request.getEmail());
                        throw new RuntimeException("Incorrect password.");
                    });
                });
    }

    @Async
    @Transactional
    @Override
    public CompletableFuture<TokenResponse> refresh(String refreshToken) {
        return CompletableFuture.supplyAsync(() -> {
            ParsedToken parsed = jwtService.parseRefresh(refreshToken);
            RefreshToken stored = refreshTokenRepository.findByIdAndUserId(parsed.jti(), parsed.userId())
                    .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(HttpStatus.UNAUTHORIZED));
            if (stored.isRevoked() || stored.getExpiresAt().isBefore(Instant.now())) {
                throw new org.springframework.web.server.ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
            stored.setRevoked(true);
            // JPA will flush on transaction commit
            return issueTokens(parsed.userId(), parsed.email());
        });
    }

    private TokenResponse issueTokens(Long userId, String email) {
        String access = jwtService.generateAccessToken(userId, email);
        String jti = UUID.randomUUID().toString();
        String refresh = jwtService.generateRefreshToken(userId, email, jti);

        RefreshToken rt = new RefreshToken();
        rt.setId(jti);
        rt.setUserId(userId);
        rt.setTokenHash(com.google.common.hash.Hashing.sha256()
                .hashString(refresh, java.nio.charset.StandardCharsets.UTF_8).toString());
        rt.setExpiresAt(Instant.now().plus(7, ChronoUnit.DAYS));
        rt.setRevoked(false);
        rt.setCreatedAt(Instant.now());
        refreshTokenRepository.save(rt);

        return new TokenResponse(access, refresh);
    }
}
