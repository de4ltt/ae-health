package feo.health.auth_service.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_credentials")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserCredentials {

    @Id
    private Long userId;

    @Column(nullable = false)
    private String passwordEncoded;
}
