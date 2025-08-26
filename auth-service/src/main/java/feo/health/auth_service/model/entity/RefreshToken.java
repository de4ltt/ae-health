package feo.health.auth_service.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@Data
public class RefreshToken {

    @Id
    private String id;

    @Column
    private Long userId;

    @Column
    private String tokenHash;

    @Column
    private Instant expiresAt;

    @Column
    private boolean revoked;

    @Column
    private String replacedBy;

    @Column
    private Instant createdAt;
}
