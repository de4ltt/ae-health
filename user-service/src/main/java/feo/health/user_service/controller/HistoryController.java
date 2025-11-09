package feo.health.user_service.controller;

import feo.health.user_service.model.dto.CatalogItemDto;
import feo.health.user_service.model.dto.DeleteHistoryItemRequest;
import feo.health.user_service.service.history.HistoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/user/history")
@AllArgsConstructor
public class HistoryController {

    private final HistoryService service;

    @DeleteMapping
    CompletableFuture<ResponseEntity<Void>> deleteHistory(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody DeleteHistoryItemRequest request
    ) {
        return service.deleteHistoryItem(userId, request).thenApply(v -> ResponseEntity.ok().build());
    }

    @GetMapping
    CompletableFuture<ResponseEntity<Map<String, List<CatalogItemDto>>>> getHistory(
            @RequestHeader("X-User-Id") Long userId
    ) {
        return service.getHistory(userId).thenApply(ResponseEntity::ok);
    }
}
