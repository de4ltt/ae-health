package feo.health.catalog_service.service.user;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user.User;
import user.UserServiceGrpc;

@Service
public class UserServiceImpl implements UserService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub stub;

    @Override
    public void saveToHistory(User.SaveToHistoryRequest request) {
        stub.saveToHistory(request);
    }
}
