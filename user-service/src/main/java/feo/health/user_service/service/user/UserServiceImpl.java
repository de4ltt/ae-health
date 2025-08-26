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
import user.User;
import user.UserServiceGrpc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@GrpcService
@RequiredArgsConstructor
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final HistoryRepository historyRepository;

    @GrpcClient("auth-service")
    private AuthServiceGrpc.AuthServiceBlockingStub stub;

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

        String email = request.getEmail();
        Optional<feo.health.user_service.model.entity.User> userOptional = userRepository.findByEmail(email);

        long userId = userOptional.isPresent() ? userOptional.get().getId() : -1L;

        User.UserIdResponse response = User.UserIdResponse.newBuilder()
                .setUserId(userId)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void saveToHistory(User.SaveToHistoryRequest request, StreamObserver<Empty> responseObserver) {

        var user = userRepository.findById(request.getUserId())
                .orElseThrow(EntityNotFoundException::new);

        History history = new History();

        HistoryId id = new HistoryId();
        id.setUserId(user.getId());
        id.setItemId(request.getItemId());
        id.setType(request.getItemType());
        id.setDateTime(LocalDateTime.now());

        history.setId(id);
        history.setUser(user);

        historyRepository.save(history);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void getUserParamsById(User.GetUserParamsByIdRequest request, StreamObserver<User.GetUserParamsByIdResponse> responseObserver) {

        Long userId = request.getUserId();
        var user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        int age = user.getDateOfBirth().toLocalDate().until(LocalDate.now()).getYears();
        Double weight = user.getWeightKg();
        Integer height = user.getHeightCm();

        User.GetUserParamsByIdResponse.Builder responseBuilder = User.GetUserParamsByIdResponse.newBuilder()
                .setAge(age);

        if (weight != null)
            responseBuilder.setWeightKg(weight);

        if (height != null)
            responseBuilder.setHeightCm(height);

        User.GetUserParamsByIdResponse response = responseBuilder.build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public UserDto getUserInfo(Long userId) {
        return userMapper.toDto(userRepository.findById(userId).orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public UserDto updateUserInfo(Long userId, UserDto userDto) {

        var user = userMapper.toEntity(userDto);
        user.setId(userId);

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void changePassword(Long userId, ChangePasswordRequest request) {
        Auth.ChangeUserPasswordRequest changeUserPasswordRequest = Auth.ChangeUserPasswordRequest.newBuilder()
                .setUserId(userId)
                .setOldPassword(request.getOldPassword())
                .setNewPassword(request.getNewPassword())
                .build();

        stub.changeUserPassword(changeUserPasswordRequest);
    }
}
