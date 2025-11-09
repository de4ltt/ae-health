package feo.health.ai_service.service.user;

import feo.health.ai_service.model.response.UserParamsDto;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import user.User;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @GrpcClient("user-service")
    private user.UserServiceGrpc.UserServiceFutureStub stub;

    @Async
    @Override
    public CompletableFuture<UserParamsDto> getUserParamsById(Long userId) {

        User.GetUserParamsByIdRequest request = User.GetUserParamsByIdRequest
                .newBuilder()
                .setUserId(userId)
                .build();

        return toCompletable(stub.getUserParamsById(request))
                .thenApply(resp ->
                        UserParamsDto.builder()
                                .age(resp.getAge())
                                .heightCm(resp.isInitialized() ? resp.getHeightCm() : null)
                                .weightKg(resp.isInitialized() ? resp.getWeightKg() : null)
                                .build()
                );
    }

    private static <T> CompletableFuture<T> toCompletable(
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

