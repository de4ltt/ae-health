package feo.health.user_service.controller;

import feo.health.user_service.model.dto.CatalogItemDto;
import feo.health.user_service.model.dto.DeleteHistoryItemRequest;
import feo.health.user_service.service.history.HistoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistoryControllerTest {

    @Mock
    private HistoryService service;

    @InjectMocks
    private HistoryController controller;

    @Test
    void deleteHistory_Success() {
        DeleteHistoryItemRequest req = new DeleteHistoryItemRequest(CatalogItemDto.builder().build(), LocalDateTime.now());
        when(service.deleteHistoryItem(eq(1L), any(DeleteHistoryItemRequest.class))).thenReturn(CompletableFuture.completedFuture(null));

        CompletableFuture<ResponseEntity<Void>> future = controller.deleteHistory(1L, req);
        ResponseEntity<Void> response = future.join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getHistory_Success() {
        Map<String, List<CatalogItemDto>> map = Map.of("DOCTOR", List.of(CatalogItemDto.builder().build()));
        when(service.getHistory(1L)).thenReturn(CompletableFuture.completedFuture(map));

        CompletableFuture<ResponseEntity<Map<String, List<CatalogItemDto>>>> future = controller.getHistory(1L);
        ResponseEntity<Map<String, List<CatalogItemDto>>> response = future.join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(map, response.getBody());
    }
}