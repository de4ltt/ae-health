package feo.health.user_service.service;

import catalog.Catalog;
import catalog.CatalogServiceGrpc;
import feo.health.user_service.mapper.CatalogItemMapper;
import feo.health.user_service.model.dto.CatalogItemDto;
import feo.health.user_service.model.dto.DeleteHistoryItemRequest;
import feo.health.user_service.model.entity.History;
import feo.health.user_service.model.entity.id_class.HistoryId;
import feo.health.user_service.repository.HistoryRepository;
import feo.health.user_service.service.history.HistoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoryServiceImplTest {

    @Mock
    private CatalogServiceGrpc.CatalogServiceFutureStub catalogStub;

    @Mock
    private CatalogItemMapper mapper;

    @Mock
    private HistoryRepository repo;

    @InjectMocks
    private HistoryServiceImpl historyService;

    @Test
    void deleteHistoryItem_Success() throws ExecutionException, InterruptedException {
        CatalogItemDto item = CatalogItemDto.builder().type("DOCTOR").link("link").build();
        DeleteHistoryItemRequest req = new DeleteHistoryItemRequest(item, LocalDateTime.now());

        com.google.common.util.concurrent.ListenableFuture<Catalog.CatalogItemIdResponse> lf = mock(com.google.common.util.concurrent.ListenableFuture.class);
        when(catalogStub.getCatalogItemId(any())).thenReturn(lf);
        Catalog.CatalogItemIdResponse resp = Catalog.CatalogItemIdResponse.newBuilder().setId(10L).build();
        when(lf.get()).thenReturn(resp);
        CompletableFuture<Void> future = historyService.deleteHistoryItem(1L, req);
        future.get();
        verify(repo).deleteById(any(HistoryId.class));
    }

    @Test
    void getHistory_Success() throws ExecutionException, InterruptedException {
        History history = new History();
        HistoryId id = new HistoryId(1L, 10L, "DOCTOR", LocalDateTime.now());
        history.setId(id);
        when(repo.findByUserId(1L)).thenReturn(List.of(history));

        com.google.common.util.concurrent.ListenableFuture<Catalog.CatalogItemsResponse> lf = mock(com.google.common.util.concurrent.ListenableFuture.class);
        when(catalogStub.getCatalogItems(any())).thenReturn(lf);
        Catalog.CatalogItem catalogItem = Catalog.CatalogItem.newBuilder().setId(10L).setType("DOCTOR").build();
        Catalog.CatalogItemsResponse resp = Catalog.CatalogItemsResponse.newBuilder().addItems(catalogItem).build();
        when(lf.get()).thenReturn(resp);

        CatalogItemDto dto = CatalogItemDto.builder().type("DOCTOR").link("link").build();
        when(mapper.toDto(catalogItem)).thenReturn(dto);

        CompletableFuture<Map<String, List<CatalogItemDto>>> future = historyService.getHistory(1L);
        Map<String, List<CatalogItemDto>> result = future.get();

        assertNotNull(result);
        assertTrue(result.containsKey("DOCTOR"));
        assertEquals(1, result.get("DOCTOR").size());
    }
}