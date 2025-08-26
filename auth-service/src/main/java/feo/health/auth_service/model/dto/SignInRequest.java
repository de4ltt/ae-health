package feo.health.auth_service.model.dto;

import lombok.Value;

@Value
public class SignInRequest {
    String email;
    String password;
}
