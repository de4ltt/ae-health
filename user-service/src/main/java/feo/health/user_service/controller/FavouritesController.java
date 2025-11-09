package feo.health.user_service.controller;

import feo.health.user_service.model.dto.CatalogItemDto;
import feo.health.user_service.model.dto.CatalogItemRequest;
import feo.health.user_service.service.favourite.FavouriteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/user/favourites")
@AllArgsConstructor
public class FavouritesController {

    private final FavouriteService service;

    @PostMapping
    CompletableFuture<ResponseEntity<Void>> addFavourite(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody CatalogItemRequest request
    ) {
        return service.addFavourite(userId, request).thenApply(v -> ResponseEntity.ok().build());
    }

    @DeleteMapping
    CompletableFuture<ResponseEntity<Void>> deleteFavourite(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody CatalogItemRequest request
    ) {
        return service.deleteFavourite(userId, request).thenApply(v -> ResponseEntity.ok().build());
    }

    @GetMapping
    CompletableFuture<ResponseEntity<Map<String, List<CatalogItemDto>>>> getFavourites(
            @RequestHeader("X-User-Id") Long userId
    ) {
        return service.getFavourites(userId).thenApply(ResponseEntity::ok);
    }
}

