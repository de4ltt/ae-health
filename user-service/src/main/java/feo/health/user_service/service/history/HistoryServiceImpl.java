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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    @GrpcClient("catalog-service")
    private CatalogServiceGrpc.CatalogServiceBlockingStub stub;

    private final CatalogItemMapper catalogItemMapper;

    private final HistoryRepository historyRepository;

    @Override
    @Transactional
    public void deleteHistoryItem(Long userId, DeleteHistoryItemRequest requestItem) {

        String type = requestItem.getCatalogItem().getType();
        String link = requestItem.getCatalogItem().getLink();
        LocalDateTime dateTime = requestItem.getDateTime();

        Catalog.CatalogItemIdRequest request = Catalog.CatalogItemIdRequest.newBuilder()
                .setType(type)
                .setLink(link)
                .build();

        Catalog.CatalogItemIdResponse response = stub.getCatalogItemId(request);

        HistoryId historyId = new HistoryId(userId, response.getId(), type, dateTime);

        historyRepository.deleteById(historyId);
    }

    @Override
    @Transactional
    public Map<String, List<CatalogItemDto>> getHistory(Long userId) {

        Map<String, List<History>> typesIdsList = historyRepository.findByUserId(userId).stream()
                .collect(Collectors.groupingBy(history -> history.getId().getType()));

        return typesIdsList.entrySet().parallelStream()
                .map(entry -> {
                    String type = entry.getKey();
                    List<History> values = entry.getValue();

                    List<Long> ids = values.stream().map(
                            h -> h.getId().getItemId()
                    ).toList();

                    Catalog.CatalogItemsRequest request = Catalog.CatalogItemsRequest.newBuilder()
                            .addAllIds(ids)
                            .setType(type)
                            .build();

                    Catalog.CatalogItemsResponse response = stub.getCatalogItems(request);

                    Map<String, CatalogItemDto> itemsMap = response.getItemsList().stream().collect(
                            Collectors.toMap(Catalog.CatalogItem::getType, catalogItemMapper::toDto)
                    );

                    List<CatalogItemDto> historyList = values.parallelStream().map(
                            item -> {
                                CatalogItemDto result = itemsMap.get(item.getId().getType());
                                result.setDateTime(item.getId().getDateTime());
                                return result;
                            }
                    ).toList();

                    return Map.entry(
                            type,
                            historyList
                    );

                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}