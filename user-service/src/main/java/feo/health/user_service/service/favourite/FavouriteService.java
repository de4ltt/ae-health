package feo.health.user_service.service.favourite;

import feo.health.user_service.model.dto.CatalogItemDto;
import feo.health.user_service.model.dto.CatalogItemRequest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface FavouriteService {
    CompletableFuture<Void> addFavourite(Long userId, CatalogItemRequest item);
    CompletableFuture<Void> deleteFavourite(Long userId, CatalogItemRequest item);
    CompletableFuture<Map<String, List<CatalogItemDto>>> getFavourites(Long userId);
}

