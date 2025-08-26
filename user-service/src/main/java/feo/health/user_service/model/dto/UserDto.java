package feo.health.user_service.model.dto;

import lombok.Builder;
import lombok.Value;

import java.sql.Date;

@Value
@Builder
public class UserDto {
    String name;
    String email;
    Date dateOfBirth;
    Double weightKg;
    Integer heightCm;
}
