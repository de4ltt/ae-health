package feo.health.auth_service.service.user;

import feo.health.auth_service.model.dto.SignUpRequest;

import java.util.Optional;

public interface UserService {
    void saveUser(SignUpRequest request);
    Optional<Long> getUserIdByEmailIfExists(String email);
}
