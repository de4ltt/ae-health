package feo.health.user_service.service;

import auth.Auth;
import auth.AuthServiceGrpc;
import com.google.protobuf.Empty;
import feo.health.user_service.mapper.UserMapper;
import feo.health.user_service.model.dto.ChangePasswordRequest;
import feo.health.user_service.model.dto.UserDto;
import feo.health.user_service.model.entity.History;
import feo.health.user_service.model.entity.User;
import feo.health.user_service.repository.HistoryRepository;
import feo.health.user_service.repository.UserRepository;
import feo.health.user_service.service.user.UserServiceImpl;
import io.grpc.stub.StreamObserver;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.google.common.util.concurrent.Futures.addCallback;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private AuthServiceGrpc.AuthServiceFutureStub authStub;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void saveUser_Success() {
        user.User.SaveUserRequest request = user.User.SaveUserRequest.newBuilder().build();
        User entity = new User();
        entity.setId(1L);
        when(userMapper.toEntity(request)).thenReturn(entity);
        when(userRepository.save(entity)).thenReturn(entity);

        StreamObserver<user.User.UserIdResponse> observer = mock(StreamObserver.class);

        userService.saveUser(request, observer);

        ArgumentCaptor<user.User.UserIdResponse> captor = ArgumentCaptor.forClass(user.User.UserIdResponse.class);
        verify(observer).onNext(captor.capture());
        verify(observer).onCompleted();
        assertEquals(1L, captor.getValue().getUserId());
    }

    @Test
    void getUserIdByEmailIfExists_Found() {
        user.User.UserByEmailRequest request = user.User.UserByEmailRequest.newBuilder().setEmail("test@example.com").build();
        User entity = new User();
        entity.setId(1L);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(entity));

        StreamObserver<user.User.UserIdResponse> observer = mock(StreamObserver.class);

        userService.getUserIdByEmailIfExists(request, observer);

        ArgumentCaptor<user.User.UserIdResponse> captor = ArgumentCaptor.forClass(user.User.UserIdResponse.class);
        verify(observer).onNext(captor.capture());
        verify(observer).onCompleted();
        assertEquals(1L, captor.getValue().getUserId());
    }

    @Test
    void getUserIdByEmailIfExists_NotFound() {
        user.User.UserByEmailRequest request = user.User.UserByEmailRequest.newBuilder().setEmail("test@example.com").build();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        StreamObserver<user.User.UserIdResponse> observer = mock(StreamObserver.class);

        userService.getUserIdByEmailIfExists(request, observer);

        ArgumentCaptor<user.User.UserIdResponse> captor = ArgumentCaptor.forClass(user.User.UserIdResponse.class);
        verify(observer).onNext(captor.capture());
        verify(observer).onCompleted();
        assertEquals(-1L, captor.getValue().getUserId());
    }

    @Test
    void saveToHistory_Success() {
        user.User.SaveToHistoryRequest request = user.User.SaveToHistoryRequest.newBuilder()
                .setUserId(1L)
                .setItemId(10L)
                .setItemType("DOCTOR")
                .build();
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        History history = new History();
        when(historyRepository.save(any(History.class))).thenReturn(history);

        StreamObserver<Empty> observer = mock(StreamObserver.class);

        userService.saveToHistory(request, observer);

        verify(historyRepository).save(any(History.class));
        verify(observer).onNext(Empty.getDefaultInstance());
        verify(observer).onCompleted();
    }

    @Test
    void saveToHistory_UserNotFound() {
        user.User.SaveToHistoryRequest request = user.User.SaveToHistoryRequest.newBuilder()
                .setUserId(1L)
                .setItemId(10L)
                .setItemType("DOCTOR")
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        StreamObserver<Empty> observer = mock(StreamObserver.class);

        assertThrows(EntityNotFoundException.class, () -> userService.saveToHistory(request, observer));
    }

    @Test
    void getUserParamsById_Success() {
        user.User.GetUserParamsByIdRequest request = user.User.GetUserParamsByIdRequest.newBuilder().setUserId(1L).build();
        User user = new User();
        user.setId(1L);
        user.setDateOfBirth(Date.valueOf(LocalDate.now().minusYears(30)));
        user.setWeightKg(70.0);
        user.setHeightCm(180);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        StreamObserver<user.User.GetUserParamsByIdResponse> observer = mock(StreamObserver.class);
        userService.getUserParamsById(request, observer);
        ArgumentCaptor<user.User.GetUserParamsByIdResponse> captor = ArgumentCaptor.forClass(user.User.GetUserParamsByIdResponse.class);
        verify(observer).onNext(captor.capture());
        verify(observer).onCompleted();
        assertEquals(30, captor.getValue().getAge());
        assertEquals(70.0, captor.getValue().getWeightKg(), 0.001);
        assertEquals(180.0, captor.getValue().getHeightCm(), 0.001);
    }

    @Test
    void getUserParamsById_UserNotFound() {
        user.User.GetUserParamsByIdRequest request = user.User.GetUserParamsByIdRequest.newBuilder().setUserId(1L).build();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        StreamObserver<user.User.GetUserParamsByIdResponse> observer = mock(StreamObserver.class);

        assertThrows(EntityNotFoundException.class, () -> userService.getUserParamsById(request, observer));
    }

    @Test
    void getUserInfo_Success() {
        User entity = new User();
        entity.setId(1L);
        UserDto dto = UserDto.builder().build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(userMapper.toDto(entity)).thenReturn(dto);

        CompletableFuture<UserDto> future = userService.getUserInfo(1L);
        UserDto result = future.join();

        assertEquals(dto, result);
    }

    @Test
    void getUserInfo_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        CompletableFuture<UserDto> future = userService.getUserInfo(1L);
        assertThrows(EntityNotFoundException.class, future::join);
    }

    @Test
    void updateUserInfo_Success() {
        UserDto dto = UserDto.builder().build();
        User entity = new User();
        entity.setId(1L);
        when(userMapper.toEntity(dto)).thenReturn(entity);
        when(userRepository.save(entity)).thenReturn(entity);
        when(userMapper.toDto(entity)).thenReturn(dto);
        CompletableFuture<UserDto> future = userService.updateUserInfo(1L, dto);
        UserDto result = future.join();
        assertEquals(dto, result);
        assertEquals(1L, entity.getId());
    }

    @Test
    void deleteUser_Success() {
        CompletableFuture<Void> future = userService.deleteUser(1L);
        future.join();
        verify(userRepository).deleteById(1L);
    }

    @Test
    void changePassword_Success() {
        ChangePasswordRequest req = new ChangePasswordRequest("old", "new");

        com.google.common.util.concurrent.ListenableFuture<Empty> lf = mock(com.google.common.util.concurrent.ListenableFuture.class);
        when(authStub.changeUserPassword(any(Auth.ChangeUserPasswordRequest.class))).thenReturn(lf);
        doAnswer(invocation -> {
            com.google.common.util.concurrent.FutureCallback<Empty> callback = invocation.getArgument(1);
            callback.onSuccess(Empty.getDefaultInstance());
            return null;
        }).when(com.google.common.util.concurrent.Futures.class);
        addCallback(eq(lf), any(), any());
        CompletableFuture<Void> future = userService.changePassword(1L, req);
        future.join();
        verify(authStub).changeUserPassword(any(Auth.ChangeUserPasswordRequest.class));
    }
}