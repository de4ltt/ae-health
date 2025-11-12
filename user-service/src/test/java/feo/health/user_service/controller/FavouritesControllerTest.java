package feo.health.user_service.controller;

import feo.health.user_service.model.dto.CatalogItemDto;
import feo.health.user_service.model.dto.CatalogItemRequest;
import feo.health.user_service.service.favourite.FavouriteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavouritesControllerTest {

    @Mock
    private FavouriteService service;

    @InjectMocks
    private FavouritesController controller;

    @Test
    void addFavourite_Success() {
        CatalogItemRequest req = new CatalogItemRequest("CLINIC", "link");
        when(service.addFavourite(eq(1L), any(CatalogItemRequest.class))).thenReturn(CompletableFuture.completedFuture(null));

        CompletableFuture<ResponseEntity<Void>> future = controller.addFavourite(1L, req);
        ResponseEntity<Void> response = future.join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteFavourite_Success() {
        CatalogItemRequest req = new CatalogItemRequest("CLINIC", "link");
        when(service.deleteFavourite(eq(1L), any(CatalogItemRequest.class))).thenReturn(CompletableFuture.completedFuture(null));

        CompletableFuture<ResponseEntity<Void>> future = controller.deleteFavourite(1L, req);
        ResponseEntity<Void> response = future.join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getFavourites_Success() {
        Map<String, List<CatalogItemDto>> map = Map.of("CLINIC", List.of(CatalogItemDto.builder().build()));
        when(service.getFavourites(1L)).thenReturn(CompletableFuture.completedFuture(map));

        CompletableFuture<ResponseEntity<Map<String, List<CatalogItemDto>>>> future = controller.getFavourites(1L);
        ResponseEntity<Map<String, List<CatalogItemDto>>> response = future.join();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(map, response.getBody());
    }
}