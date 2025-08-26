package feo.health.user_service.model.dto;

import lombok.Value;

@Value
public class ChangePasswordRequest {
    String oldPassword;
    String newPassword;
}
