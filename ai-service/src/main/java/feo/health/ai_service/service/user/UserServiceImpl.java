package feo.health.ai_service.service.user;

import feo.health.ai_service.model.response.UserParamsDto;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import user.User;

@Service
public class UserServiceImpl implements UserService {

    @GrpcClient("user-service")
    private user.UserServiceGrpc.UserServiceBlockingStub stub;

    @Override
    public UserParamsDto getUserParamsById(Long userId) {

        User.GetUserParamsByIdRequest request = User.GetUserParamsByIdRequest.newBuilder()
                .setUserId(userId)
                .build();

        User.GetUserParamsByIdResponse response = stub.getUserParamsById(request);

        return UserParamsDto.builder()
                .age(response.getAge())
                .heightCm(response.getHeightCm())
                .weightKg(response.getWeightKg())
                .build();
    }
}
