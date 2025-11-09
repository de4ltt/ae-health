package feo.health.ai_service.service.user;

import feo.health.ai_service.model.response.UserParamsDto;

import java.util.concurrent.CompletableFuture;

public interface UserService {
    CompletableFuture<UserParamsDto> getUserParamsById(Long userId);
}
