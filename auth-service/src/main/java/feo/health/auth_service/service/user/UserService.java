package feo.health.auth_service.service.user;

import feo.health.auth_service.model.dto.SignUpRequest;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface UserService {
    CompletableFuture<Void> saveUser(SignUpRequest request);
    CompletableFuture<Optional<Long>> getUserIdByEmailIfExists(String email);
}
