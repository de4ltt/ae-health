package feo.health.catalog_service.service.user;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import user.User;
import user.UserServiceGrpc;

import java.util.concurrent.CompletableFuture;

@Service
public class UserServiceImpl implements UserService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceFutureStub stub;

    @Override
    @Async
    public CompletableFuture<Void> saveToHistory(User.SaveToHistoryRequest request) {
        return CompletableFuture.runAsync(() -> stub.saveToHistory(request));
    }
}