package feo.health.catalog_service.service.user;

import user.User;

public interface UserService {
    void saveToHistory(User.SaveToHistoryRequest request);
}
