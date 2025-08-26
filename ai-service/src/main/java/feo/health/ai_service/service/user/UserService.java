package feo.health.ai_service.service.user;

import feo.health.ai_service.model.response.UserParamsDto;

public interface UserService {
    UserParamsDto getUserParamsById(Long userId);
}
