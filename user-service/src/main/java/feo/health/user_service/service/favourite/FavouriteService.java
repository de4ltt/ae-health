package feo.health.user_service.service.favourite;

import feo.health.user_service.model.dto.CatalogItemDto;
import feo.health.user_service.model.dto.CatalogItemRequest;

import java.util.List;
import java.util.Map;

public interface FavouriteService {
    void addFavourite(Long userId, CatalogItemRequest item);
    void deleteFavourite(Long userId, CatalogItemRequest item);
    Map<String, List<CatalogItemDto>>  getFavourites(Long userId);
}
