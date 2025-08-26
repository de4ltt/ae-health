package feo.health.user_service.service.history;

import feo.health.user_service.model.dto.CatalogItemDto;
import feo.health.user_service.model.dto.DeleteHistoryItemRequest;

import java.util.List;
import java.util.Map;

public interface HistoryService {
    void deleteHistoryItem(Long userId, DeleteHistoryItemRequest request);
    Map<String, List<CatalogItemDto>> getHistory(Long userId);
}

