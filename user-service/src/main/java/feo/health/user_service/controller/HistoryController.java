package feo.health.user_service.controller;

import feo.health.user_service.model.dto.CatalogItemDto;
import feo.health.user_service.model.dto.DeleteHistoryItemRequest;
import feo.health.user_service.service.history.HistoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user/history")
@AllArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @DeleteMapping
    ResponseEntity<Void> deleteHistoryItem(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody DeleteHistoryItemRequest request
    ) {
        historyService.deleteHistoryItem(userId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    ResponseEntity<Map<String, List<CatalogItemDto>>> getHistory(
            @RequestHeader("X-User-Id") Long userId
    ) {
        return ResponseEntity.ok(historyService.getHistory(userId));
    }
}
