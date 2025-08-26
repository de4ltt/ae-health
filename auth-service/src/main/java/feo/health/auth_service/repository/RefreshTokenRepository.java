package feo.health.auth_service.repository;

import feo.health.auth_service.model.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByIdAndUserId(String id, Long userId);
}
