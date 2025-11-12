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
import lombok.Setter;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavouriteServiceImpl implements FavouriteService {

    @GrpcClient("catalog-service")
    private final CatalogServiceGrpc.CatalogServiceFutureStub stub;

    private final CatalogItemMapper mapper;
    private final FavouriteRepository repo;
    private final UserRepository userRepo;

    @Async
    @Override
    public CompletableFuture<Void> addFavourite(Long userId, CatalogItemRequest item) {

        Catalog.CatalogItemIdRequest req = Catalog.CatalogItemIdRequest.newBuilder()
                .setType(item.getType())
                .setLink(item.getLink())
                .build();

        return toCF(stub.getCatalogItemId(req)).thenAccept(resp -> {
            User user = userRepo.findById(userId).orElseThrow();

            FavouriteId id = new FavouriteId(userId, resp.getId(), item.getType());

            Favourite fav = Favourite.builder()
                    .user(user)
                    .id(id)
                    .dateTime(LocalDateTime.now())
                    .build();

            repo.save(fav);
        });
    }

    @Async
    @Override
    public CompletableFuture<Void> deleteFavourite(Long userId, CatalogItemRequest item) {

        Catalog.CatalogItemIdRequest req = Catalog.CatalogItemIdRequest.newBuilder()
                .setType(item.getType())
                .setLink(item.getLink())
                .build();

        return toCF(stub.getCatalogItemId(req)).thenAccept(resp -> {
            FavouriteId id = new FavouriteId(userId, resp.getId(), item.getType());
            repo.deleteById(id);
        });
    }

    @Async
    @Override
    public CompletableFuture<Map<String, List<CatalogItemDto>>> getFavourites(Long userId) {

        return CompletableFuture.supplyAsync(() -> {
            Map<String, List<Favourite>> groups = repo.findByUserId(userId).stream()
                    .collect(Collectors.groupingBy(f -> f.getId().getType()));

            return groups.entrySet().parallelStream()
                    .map(entry -> {
                        String type = entry.getKey();
                        List<Long> ids = entry.getValue().stream()
                                .map(f -> f.getId().getItemId())
                                .toList();

                        Catalog.CatalogItemsRequest req = Catalog.CatalogItemsRequest.newBuilder()
                                .addAllIds(ids)
                                .setType(type)
                                .build();

                        Catalog.CatalogItemsResponse resp;

                        try {
                            resp = stub.getCatalogItems(req).get();

                            Map<String, CatalogItemDto> itemMap = resp.getItemsList().stream()
                                    .collect(Collectors.toMap(
                                            Catalog.CatalogItem::getType,
                                            mapper::toDto
                                    ));

                            List<CatalogItemDto> list = entry.getValue().stream()
                                    .map(f -> {
                                        CatalogItemDto dto = itemMap.get(f.getId().getType());
                                        dto.setDateTime(f.getDateTime());
                                        return dto;
                                    })
                                    .toList();

                            return Map.entry(type, list);
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        });
    }

    private static <T> CompletableFuture<T> toCF(com.google.common.util.concurrent.ListenableFuture<T> lf) {
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
