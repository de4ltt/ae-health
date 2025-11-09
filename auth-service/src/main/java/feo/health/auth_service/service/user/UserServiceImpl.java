package feo.health.auth_service.service.user;

import feo.health.auth_service.mapper.UserMapper;
import feo.health.auth_service.model.dto.SignUpRequest;
import feo.health.auth_service.model.entity.UserCredentials;
import feo.health.auth_service.repository.UserCredentialsRepository;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import user.User;
import user.UserServiceGrpc;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceFutureStub stub;

    private final UserMapper userMapper;
    private final UserCredentialsRepository userCredentialsRepository;

    @Async
    @Override
    public CompletableFuture<Void> saveUser(SignUpRequest userRequest) {
        User.SaveUserRequest req = userMapper.toSaveUserRequest(userRequest);
        return toCompletable(stub.saveUser(req))
                .thenAccept(response -> {
                    UserCredentials creds = userMapper.toUserCredentials(response, userRequest.getPassword());
                    userCredentialsRepository.save(creds);
                })
                .exceptionally(ex -> null);
    }

    @Async
    @Override
    public CompletableFuture<Optional<Long>> getUserIdByEmailIfExists(String email) {
        User.UserByEmailRequest req = User.UserByEmailRequest.newBuilder().setEmail(email).build();
        return toCompletable(stub.getUserIdByEmailIfExists(req))
                .thenApply(r -> r.getUserId() != -1L ? Optional.of(r.getUserId()) : Optional.empty());
    }

    private static <T> CompletableFuture<T> toCompletable(com.google.common.util.concurrent.ListenableFuture<T> lf) {
        CompletableFuture<T> cf = new CompletableFuture<>();
        com.google.common.util.concurrent.Futures.addCallback(
                lf,
                new com.google.common.util.concurrent.FutureCallback<>() {
                    @Override public void onSuccess(T result) { cf.complete(result); }
                    @Override public void onFailure(Throwable t) { cf.completeExceptionally(t); }
                },
                com.google.common.util.concurrent.MoreExecutors.directExecutor()
        );
        return cf;
    }
}

