package feo.health.user_service.service.history;

import catalog.Catalog;
import catalog.CatalogServiceGrpc;
import feo.health.user_service.mapper.CatalogItemMapper;
import feo.health.user_service.model.dto.CatalogItemDto;
import feo.health.user_service.model.dto.DeleteHistoryItemRequest;
import feo.health.user_service.model.entity.History;
import feo.health.user_service.model.entity.id_class.HistoryId;
import feo.health.user_service.repository.HistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistoryServiceImpl implements HistoryService {

    @GrpcClient("catalog-service")
    private CatalogServiceGrpc.CatalogServiceFutureStub stub;

    private final CatalogItemMapper mapper;
    private final HistoryRepository repo;

    @Async
    @Override
    @Transactional
    public CompletableFuture<Void> deleteHistoryItem(Long userId, DeleteHistoryItemRequest req) {
        log.info("Deleting history item for userId={}, request={}", userId, req);

        Catalog.CatalogItemIdRequest idReq = Catalog.CatalogItemIdRequest.newBuilder()
                .setType(req.getCatalogItem().getType())
                .setLink(req.getCatalogItem().getLink())
                .build();

        return toCF(stub.getCatalogItemId(idReq))
                .thenAccept(resp -> {
                    log.debug("Received catalog item id response: {}", resp);

                    HistoryId hid = new HistoryId(
                            userId,
                            resp.getId(),
                            req.getCatalogItem().getType(),
                            req.getDateTime()
                    );
                    repo.deleteById(hid);
                    log.info("Deleted history item with id: {}", hid);
                })
                .exceptionally(ex -> {
                    log.error("Failed to delete history item for userId=" + userId, ex);
                    throw new RuntimeException(ex);
                });
    }

    @Async
    @Override
    @Transactional
    public CompletableFuture<Map<String, List<CatalogItemDto>>> getHistory(Long userId) {
        log.info("Fetching history for userId={}", userId);
        return CompletableFuture.supplyAsync(() -> {
            Map<String, List<History>> groups = repo.findByUserId(userId).stream()
                    .peek(h -> log.debug("Found history entry: {}", h))
                    .collect(Collectors.groupingBy(h -> h.getId().getType()));

            Map<String, List<CatalogItemDto>> resultMap = groups.entrySet().parallelStream()
                    .map(entry -> {
                        String type = entry.getKey();
                        List<Long> ids = entry.getValue().stream()
                                .map(h -> h.getId().getItemId())
                                .toList();

                        log.debug("Requesting catalog items for type={}, ids={}", type, ids);

                        Catalog.CatalogItemsRequest req = Catalog.CatalogItemsRequest.newBuilder()
                                .addAllIds(ids)
                                .setType(type)
                                .build();

                        try {
                            Catalog.CatalogItemsResponse resp = stub.getCatalogItems(req).get();
                            log.debug("Received catalog items response for type={}", type);

                            Map<Long, CatalogItemDto> map = resp.getItemsList().stream()
                                    .collect(Collectors.toMap(
                                            Catalog.CatalogItem::getId,
                                            mapper::toDto
                                    ));

                            List<CatalogItemDto> result = entry.getValue().stream()
                                    .map(h -> {
                                        Long key = h.getId().getItemId();
                                        CatalogItemDto dto = map.get(key);
                                        if (dto != null) {
                                            dto.setDateTime(h.getId().getDateTime());
                                        } else {
                                            dto = CatalogItemDto.builder()
                                                    .dateTime(h.getId().getDateTime())
                                                    .build();
                                            log.warn("Catalog item DTO not found for itemId={}", key);
                                        }
                                        return dto;
                                    })
                                    .toList();

                            return Map.entry(type, result);
                        } catch (InterruptedException | ExecutionException e) {
                            log.error("Error fetching catalog items for type=" + type, e);
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            log.info("Fetched history map for userId={}", userId);
            return resultMap;
        });
    }

    private static <T> CompletableFuture<T> toCF(
            com.google.common.util.concurrent.ListenableFuture<T> lf
    ) {
        CompletableFuture<T> cf = new CompletableFuture<>();
        com.google.common.util.concurrent.Futures.addCallback(
                lf,
                new com.google.common.util.concurrent.FutureCallback<>() {
                    @Override
                    public void onSuccess(T r) {
                        cf.complete(r);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        cf.completeExceptionally(t);
                    }
                },
                com.google.common.util.concurrent.MoreExecutors.directExecutor()
        );
        return cf;
    }
}
