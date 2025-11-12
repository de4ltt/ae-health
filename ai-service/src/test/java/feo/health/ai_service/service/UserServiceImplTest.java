package feo.health.ai_service.service;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import feo.health.ai_service.model.response.UserParamsDto;
import feo.health.ai_service.service.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import user.User;
import user.UserServiceGrpc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserServiceGrpc.UserServiceFutureStub stub;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUserParamsById_successWithValues() throws Exception {
        Long userId = 1L;

        User.GetUserParamsByIdResponse resp = User.GetUserParamsByIdResponse.newBuilder()
                .setAge(30)
                .setHeightCm(180)
                .setWeightKg(70.0)
                .build();

        ListenableFuture<User.GetUserParamsByIdResponse> lf = Futures.immediateFuture(resp);
        when(stub.getUserParamsById(any(User.GetUserParamsByIdRequest.class))).thenReturn(lf);

        CompletableFuture<UserParamsDto> future = userService.getUserParamsById(userId);
        UserParamsDto dto = future.get();

        assertNotNull(dto);
        assertEquals(30, dto.getAge());
        assertEquals(Integer.valueOf(180), dto.getHeightCm());
        assertEquals(Double.valueOf(70.0), dto.getWeightKg());
    }

    @Test
    void getUserParamsById_successWithDefaults() throws Exception {
        Long userId = 1L;

        User.GetUserParamsByIdResponse resp = User.GetUserParamsByIdResponse.newBuilder()
                .setAge(30)
                .build();

        ListenableFuture<User.GetUserParamsByIdResponse> lf = Futures.immediateFuture(resp);
        when(stub.getUserParamsById(any(User.GetUserParamsByIdRequest.class))).thenReturn(lf);

        CompletableFuture<UserParamsDto> future = userService.getUserParamsById(userId);
        UserParamsDto dto = future.get();

        assertNotNull(dto);
        assertEquals(30, dto.getAge());
        assertEquals(Integer.valueOf(0), dto.getHeightCm());
        assertEquals(Double.valueOf(0.0), dto.getWeightKg());
    }

    @Test
    void getUserParamsById_failure() {
        Long userId = 1L;

        ListenableFuture<User.GetUserParamsByIdResponse> lf = Futures.immediateFailedFuture(new RuntimeException("gRPC error"));
        when(stub.getUserParamsById(any(User.GetUserParamsByIdRequest.class))).thenReturn(lf);

        CompletableFuture<UserParamsDto> future = userService.getUserParamsById(userId);

        ExecutionException exception = assertThrows(ExecutionException.class, future::get);
        assertInstanceOf(RuntimeException.class, exception.getCause());
        assertEquals("gRPC error", exception.getCause().getMessage());
    }
}