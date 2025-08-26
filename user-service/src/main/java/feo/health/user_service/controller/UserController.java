package feo.health.user_service.controller;

import feo.health.user_service.model.dto.ChangePasswordRequest;
import feo.health.user_service.model.dto.UserDto;
import feo.health.user_service.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    ResponseEntity<UserDto> getUserInfo(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    @PutMapping
    ResponseEntity<UserDto> updateUserInfo(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody UserDto newData
    ) {
        return ResponseEntity.ok(userService.updateUserInfo(userId, newData));
    }

    @DeleteMapping
    ResponseEntity<Void> deleteUser(
            @RequestHeader("X-User-Id") Long userId
    ) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password")
    ResponseEntity<Void> changePassword(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody ChangePasswordRequest request
    ) {
        userService.changePassword(userId, request);
        return ResponseEntity.ok().build();
    }
}
