package feo.health.auth_service.service.user;

import feo.health.auth_service.mapper.UserMapper;
import feo.health.auth_service.model.dto.SignUpRequest;
import feo.health.auth_service.model.entity.UserCredentials;
import feo.health.auth_service.repository.UserCredentialsRepository;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import user.User;
import user.UserServiceGrpc;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub stub;

    private final UserMapper userMapper;

    private final UserCredentialsRepository userCredentialsRepository;

    @Override
    public void saveUser(SignUpRequest userRequest) {

        User.SaveUserRequest request = userMapper.toSaveUserRequest(userRequest);

        try {
            User.UserIdResponse response = stub.saveUser(request);

            UserCredentials userCredentials = userMapper.toUserCredentials(response, userRequest.getPassword());
            userCredentialsRepository.save(userCredentials);
        } catch (Exception ignored) {}
    }

    @Override
    public Optional<Long> getUserIdByEmailIfExists(String email) {

        User.UserByEmailRequest request = User.UserByEmailRequest.newBuilder()
                .setEmail(email)
                .build();

        User.UserIdResponse response = stub.getUserIdByEmailIfExists(request);
        long userId = response.getUserId();

        return userId != -1L ? Optional.of(userId) : Optional.empty();
    }
}
