package feo.health.catalog_service.service.user;

import user.User;

import java.util.concurrent.CompletableFuture;

public interface UserService {
    CompletableFuture<Void> saveToHistory(User.SaveToHistoryRequest request);
}