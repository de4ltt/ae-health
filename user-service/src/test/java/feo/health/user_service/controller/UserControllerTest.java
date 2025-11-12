package feo.health.user_service.controller;

import feo.health.user_service.model.dto.ChangePasswordRequest;
import feo.health.user_service.model.dto.UserDto;
import feo.health.user_service.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;

    @Test
    void getUserInfo_Success() {
        UserDto dto = UserDto.builder().build();
        when(service.getUserInfo(1L)).thenReturn(CompletableFuture.completedFuture(dto));

        CompletableFuture<ResponseEntity<UserDto>> future = controller.getUserInfo(1L);
        ResponseEntity<UserDto> response = future.join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void updateUserInfo_Success() {
        UserDto dto = UserDto.builder().build();
        when(service.updateUserInfo(eq(1L), any(UserDto.class))).thenReturn(CompletableFuture.completedFuture(dto));

        CompletableFuture<ResponseEntity<UserDto>> future = controller.updateUserInfo(1L, dto);
        ResponseEntity<UserDto> response = future.join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void deleteUser_Success() {
        when(service.deleteUser(1L)).thenReturn(CompletableFuture.completedFuture(null));

        CompletableFuture<ResponseEntity<Void>> future = controller.deleteUser(1L);
        ResponseEntity<Void> response = future.join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void changePassword_Success() {
        ChangePasswordRequest req = new ChangePasswordRequest("old", "new");
        when(service.changePassword(eq(1L), any(ChangePasswordRequest.class))).thenReturn(CompletableFuture.completedFuture(null));

        CompletableFuture<ResponseEntity<Void>> future = controller.changePassword(1L, req);
        ResponseEntity<Void> response = future.join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}