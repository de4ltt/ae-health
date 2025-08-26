package feo.health.auth_service.mapper;

import feo.health.auth_service.model.dto.SignUpRequest;
import feo.health.auth_service.model.entity.UserCredentials;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import user.User;

@Component
@AllArgsConstructor
public class UserMapper {

    private PasswordEncoder passwordEncoder;

    public User.SaveUserRequest toSaveUserRequest(SignUpRequest request) {
        return User.SaveUserRequest.newBuilder()
                .setName(request.getName())
                .setEmail(request.getEmail())
                .setDateOfBirth(request.getDateOfBirth().toString())
                .build();
    }

    public UserCredentials toUserCredentials(User.UserIdResponse response, String password) {
        return UserCredentials.builder()
                .userId(response.getUserId())
                .passwordEncoded(passwordEncoder.encode(password))
                .build();
    }
}
