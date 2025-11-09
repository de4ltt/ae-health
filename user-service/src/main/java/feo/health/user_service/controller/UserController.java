package feo.health.user_service.controller;

import feo.health.user_service.model.dto.ChangePasswordRequest;
import feo.health.user_service.model.dto.UserDto;
import feo.health.user_service.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    CompletableFuture<ResponseEntity<UserDto>> getUserInfo(
            @RequestHeader("X-User-Id") Long userId
    ) {
        return service.getUserInfo(userId).thenApply(ResponseEntity::ok);
    }

    @PutMapping
    CompletableFuture<ResponseEntity<UserDto>> updateUserInfo(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody UserDto dto
    ) {
        return service.updateUserInfo(userId, dto).thenApply(ResponseEntity::ok);
    }

    @DeleteMapping
    CompletableFuture<ResponseEntity<Void>> deleteUser(
            @RequestHeader("X-User-Id") Long userId
    ) {
        return service.deleteUser(userId).thenApply(v -> ResponseEntity.ok().build());
    }

    @PostMapping("/password")
    CompletableFuture<ResponseEntity<Void>> changePassword(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody ChangePasswordRequest req
    ) {
        return service.changePassword(userId, req)
                .thenApply(v -> ResponseEntity.ok().build());
    }
}

