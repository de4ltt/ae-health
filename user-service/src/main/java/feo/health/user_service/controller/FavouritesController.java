package feo.health.user_service.controller;

import feo.health.user_service.model.dto.CatalogItemDto;
import feo.health.user_service.model.dto.CatalogItemRequest;
import feo.health.user_service.service.favourite.FavouriteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user/favourites")
@AllArgsConstructor
public class FavouritesController {

    private final FavouriteService favouriteService;

    @PostMapping
    ResponseEntity<Void> addFavourite(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody CatalogItemRequest request
    ) {
        favouriteService.addFavourite(userId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    ResponseEntity<Void> deleteFavourite(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody CatalogItemRequest request
    ) {
        favouriteService.deleteFavourite(userId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    ResponseEntity<Map<String, List<CatalogItemDto>>> getFavourites(
            @RequestHeader("X-User-Id") Long userId
    ) {
        return ResponseEntity.ok(favouriteService.getFavourites(userId));
    }
}
