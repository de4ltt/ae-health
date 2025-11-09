package feo.health.user_service.service.history;

import feo.health.user_service.model.dto.CatalogItemDto;
import feo.health.user_service.model.dto.DeleteHistoryItemRequest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface HistoryService {
    CompletableFuture<Void> deleteHistoryItem(Long userId, DeleteHistoryItemRequest request);
    CompletableFuture<Map<String, List<CatalogItemDto>>> getHistory(Long userId);
}


