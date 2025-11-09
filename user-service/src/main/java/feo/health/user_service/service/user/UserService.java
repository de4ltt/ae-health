package feo.health.user_service.service.user;

import feo.health.user_service.model.dto.ChangePasswordRequest;
import feo.health.user_service.model.dto.UserDto;

import java.util.concurrent.CompletableFuture;

public interface UserService {
    CompletableFuture<UserDto> getUserInfo(Long userId);
    CompletableFuture<UserDto> updateUserInfo(Long userId, UserDto userDto);
    CompletableFuture<Void> deleteUser(Long id);
    CompletableFuture<Void> changePassword(Long userId, ChangePasswordRequest request);
}
