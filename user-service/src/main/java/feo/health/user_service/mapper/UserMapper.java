package feo.health.user_service.mapper;

import feo.health.user_service.model.dto.UserDto;
import feo.health.user_service.model.entity.User;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class UserMapper {

    public User toEntity(user.User.SaveUserRequest userRequest) {
        return User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .dateOfBirth(Date.valueOf(userRequest.getDateOfBirth()))
                .build();
    }

    public User toEntity(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .dateOfBirth(userDto.getDateOfBirth())
                .heightCm(userDto.getHeightCm())
                .weightKg(userDto.getWeightKg())
                .build();
    }

    public UserDto toDto(User user) {
        return UserDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .heightCm(user.getHeightCm())
                .weightKg(user.getWeightKg())
                .build();
    }
}
