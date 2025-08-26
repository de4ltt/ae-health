package feo.health.user_service.service.favourite;

import catalog.Catalog;
import catalog.CatalogServiceGrpc;
import feo.health.user_service.mapper.CatalogItemMapper;
import feo.health.user_service.model.dto.CatalogItemDto;
import feo.health.user_service.model.dto.CatalogItemRequest;
import feo.health.user_service.model.entity.Favourite;
import feo.health.user_service.model.entity.User;
import feo.health.user_service.model.entity.id_class.FavouriteId;
import feo.health.user_service.repository.FavouriteRepository;
import feo.health.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavouriteServiceImpl implements FavouriteService {

    @GrpcClient("catalog-service")
    private CatalogServiceGrpc.CatalogServiceBlockingStub stub;

    private final CatalogItemMapper catalogItemMapper;

    private final FavouriteRepository favouriteRepository;
    private final UserRepository userRepository;

    @Override
    public void addFavourite(Long userId, CatalogItemRequest item) {

        Catalog.CatalogItemIdRequest request = Catalog.CatalogItemIdRequest.newBuilder()
                .setType(item.getType())
                .setLink(item.getLink())
                .build();

        Catalog.CatalogItemIdResponse response = stub.getCatalogItemId(request);

        User user = userRepository.findById(userId).orElseThrow();

        FavouriteId id = new FavouriteId();
        id.setType(item.getType());
        id.setItemId(response.getId());
        id.setUserId(userId);

        Favourite favourite = Favourite.builder()
                .user(user)
                .id(id)
                .dateTime(LocalDateTime.now())
                .build();

        favouriteRepository.save(favourite);
    }

    @Override
    public void deleteFavourite(Long userId, CatalogItemRequest item) {

        Catalog.CatalogItemIdRequest request = Catalog.CatalogItemIdRequest.newBuilder()
                .setType(item.getType())
                .setLink(item.getLink())
                .build();

        Catalog.CatalogItemIdResponse response = stub.getCatalogItemId(request);

        FavouriteId favouriteId = new FavouriteId(userId, response.getId(), item.getType());

        favouriteRepository.deleteById(favouriteId);
    }

    @Override
    public Map<String, List<CatalogItemDto>> getFavourites(Long userId) {

        Map<String, List<Favourite>> typesIdsList = favouriteRepository.findByUserId(userId).stream()
                .collect(Collectors.groupingBy(favourite -> favourite.getId().getType()));

        return typesIdsList.entrySet().parallelStream()
                .map(entry -> {
                    String type = entry.getKey();
                    List<Favourite> values = entry.getValue();

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

                    List<CatalogItemDto> favouriteList = values.parallelStream().map(
                            item -> {
                                CatalogItemDto result = itemsMap.get(item.getId().getType());
                                result.setDateTime(item.getDateTime());
                                return result;
                            }
                    ).toList();

                    return Map.entry(
                            type,
                            favouriteList
                    );

                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}