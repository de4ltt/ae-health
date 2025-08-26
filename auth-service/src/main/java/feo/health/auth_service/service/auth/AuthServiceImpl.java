package feo.health.auth_service.service.auth;

import auth.Auth;
import auth.AuthServiceGrpc;
import com.google.common.hash.Hashing;
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
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@GrpcService
@AllArgsConstructor
public class AuthServiceImpl extends AuthServiceGrpc.AuthServiceImplBase implements AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    private UserCredentialsRepository userCredentialsRepository;

    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void changeUserPassword(Auth.ChangeUserPasswordRequest request, StreamObserver<Empty> responseObserver) {

        UserCredentials userCredentials = userCredentialsRepository.findById(request.getUserId())
                .orElseThrow(EntityNotFoundException::new);

        if (!passwordEncoder.matches(request.getOldPassword(), userCredentials.getPasswordEncoded()))
            throw new RuntimeException("Wrong password.");

        userCredentials.setPasswordEncoded(passwordEncoder.encode(request.getNewPassword()));
        userCredentialsRepository.save(userCredentials);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void signUp(SignUpRequest request) {

        if (userService.getUserIdByEmailIfExists(request.getEmail()).isPresent())
            throw new RuntimeException("User already exists.");

        userService.saveUser(request);
    }

    @Override
    public TokenResponse signIn(SignInRequest request) {

        Optional<Long> userIdOptional = userService.getUserIdByEmailIfExists(request.getEmail());

        if (userIdOptional.isEmpty())
            throw new RuntimeException("User does not exist.");

        Long userId = userIdOptional.get();
        UserCredentials userCredentials = userCredentialsRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User does not exist."));

        if (passwordEncoder.matches(request.getPassword(), userCredentials.getPasswordEncoded()))
            return issueTokens(userId, request.getEmail());

        throw new RuntimeException("Incorrect password.");
    }

    @Override
    @Transactional
    public TokenResponse refresh(String refreshToken) {

        ParsedToken parsed = jwtService.parseRefresh(refreshToken);

        RefreshToken stored = refreshTokenRepository.findByIdAndUserId(parsed.jti(), parsed.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        if (stored.isRevoked() || stored.getExpiresAt().isBefore(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        stored.setRevoked(true);

        return issueTokens(parsed.userId(), parsed.email());
    }

    @Transactional
    private TokenResponse issueTokens(Long userId, String email) {

        String access = jwtService.generateAccessToken(userId, email);
        String jti = UUID.randomUUID().toString();
        String refresh = jwtService.generateRefreshToken(userId, email, jti);

        RefreshToken rt = new RefreshToken();
        rt.setId(jti);
        rt.setUserId(userId);
        rt.setTokenHash(Hashing.sha256().hashString(refresh, StandardCharsets.UTF_8).toString());
        rt.setExpiresAt(Instant.now().plus(7, ChronoUnit.DAYS));
        rt.setRevoked(false);
        rt.setCreatedAt(Instant.now());
        refreshTokenRepository.save(rt);

        return new TokenResponse(access, refresh);
    }
}
