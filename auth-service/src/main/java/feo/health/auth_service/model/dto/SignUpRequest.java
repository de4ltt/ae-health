package feo.health.auth_service.model.dto;

import lombok.Value;

import java.sql.Date;

@Value
public class SignUpRequest {
    String name;
    String email;
    String password;
    Date dateOfBirth;
}
