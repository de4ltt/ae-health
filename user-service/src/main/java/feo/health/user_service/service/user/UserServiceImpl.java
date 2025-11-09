package feo.health.user_service.service.user;

import auth.Auth;
import auth.AuthServiceGrpc;
import com.google.protobuf.Empty;
import feo.health.user_service.mapper.UserMapper;
import feo.health.user_service.model.dto.ChangePasswordRequest;
import feo.health.user_service.model.dto.UserDto;
import feo.health.user_service.model.entity.History;
import feo.health.user_service.model.entity.id_class.HistoryId;
import feo.health.user_service.repository.HistoryRepository;
import feo.health.user_service.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.scheduling.annotation.Async;
import user.User;
import user.UserServiceGrpc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@GrpcService
@RequiredArgsConstructor
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final HistoryRepository historyRepository;

    @GrpcClient("auth-service")
    private AuthServiceGrpc.AuthServiceFutureStub stub;

    @Override
    public void saveUser(User.SaveUserRequest request, StreamObserver<User.UserIdResponse> responseObserver) {
        var user = userRepository.save(userMapper.toEntity(request));

        User.UserIdResponse response = User.UserIdResponse.newBuilder()
                .setUserId(user.getId())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUserIdByEmailIfExists(User.UserByEmailRequest request, StreamObserver<User.UserIdResponse> responseObserver) {
        Optional<feo.health.user_service.model.entity.User> u = userRepository.findByEmail(request.getEmail());

        long id = u.map(feo.health.user_service.model.entity.User::getId).orElse(-1L);

        responseObserver.onNext(
                User.UserIdResponse.newBuilder().setUserId(id).build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void saveToHistory(User.SaveToHistoryRequest request, StreamObserver<Empty> responseObserver) {

        var user = userRepository.findById(request.getUserId())
                .orElseThrow(EntityNotFoundException::new);

        HistoryId id = new HistoryId(
                user.getId(),
                request.getItemId(),
                request.getItemType(),
                LocalDateTime.now()
        );

        History history = new History();
        history.setId(id);
        history.setUser(user);

        historyRepository.save(history);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void getUserParamsById(User.GetUserParamsByIdRequest request, StreamObserver<User.GetUserParamsByIdResponse> responseObserver) {

        var user = userRepository.findById(request.getUserId())
                .orElseThrow(EntityNotFoundException::new);

        int age = user.getDateOfBirth().toLocalDate().until(LocalDate.now()).getYears();

        var b = User.GetUserParamsByIdResponse.newBuilder().setAge(age);

        if (user.getWeightKg() != null) b.setWeightKg(user.getWeightKg());
        if (user.getHeightCm() != null) b.setHeightCm(user.getHeightCm());

        responseObserver.onNext(b.build());
        responseObserver.onCompleted();
    }

    @Async
    @Override
    public CompletableFuture<UserDto> getUserInfo(Long userId) {
        return CompletableFuture.supplyAsync(() ->
                userMapper.toDto(
                        userRepository.findById(userId).orElseThrow(EntityNotFoundException::new)
                )
        );
    }

    @Async
    @Override
    public CompletableFuture<UserDto> updateUserInfo(Long userId, UserDto userDto) {
        return CompletableFuture.supplyAsync(() -> {
            var entity = userMapper.toEntity(userDto);
            entity.setId(userId);
            return userMapper.toDto(userRepository.save(entity));
        });
    }

    @Async
    @Override
    public CompletableFuture<Void> deleteUser(Long id) {
        return CompletableFuture.runAsync(() -> userRepository.deleteById(id));
    }

    @Async
    @Override
    public CompletableFuture<Void> changePassword(Long userId, ChangePasswordRequest req) {
        Auth.ChangeUserPasswordRequest grpcReq = Auth.ChangeUserPasswordRequest.newBuilder()
                .setUserId(userId)
                .setOldPassword(req.getOldPassword())
                .setNewPassword(req.getNewPassword())
                .build();

        return toCF(stub.changeUserPassword(grpcReq)).thenApply(v -> null);
    }


    private static <T> CompletableFuture<T> toCF(
            com.google.common.util.concurrent.ListenableFuture<T> lf
    ) {
        CompletableFuture<T> cf = new CompletableFuture<>();
        com.google.common.util.concurrent.Futures.addCallback(
                lf,
                new com.google.common.util.concurrent.FutureCallback<>() {
                    @Override public void onSuccess(T r) { cf.complete(r); }
                    @Override public void onFailure(Throwable t) { cf.completeExceptionally(t); }
                },
                com.google.common.util.concurrent.MoreExecutors.directExecutor()
        );
        return cf;
    }
}

