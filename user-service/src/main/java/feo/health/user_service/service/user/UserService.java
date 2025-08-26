package feo.health.user_service.service.user;

import feo.health.user_service.model.dto.ChangePasswordRequest;
import feo.health.user_service.model.dto.UserDto;

public interface UserService {
    UserDto getUserInfo(Long userId);
    UserDto updateUserInfo(Long userId, UserDto userDto);
    void deleteUser(Long id);
    void changePassword(Long userId, ChangePasswordRequest request);
}
