package feo.health.user_service.service;

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
import feo.health.user_service.service.favourite.FavouriteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavouriteServiceImplTest {

    @Mock
    private CatalogServiceGrpc.CatalogServiceFutureStub stub;

    @Mock
    private CatalogItemMapper mapper;

    @Mock
    private FavouriteRepository repo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private FavouriteServiceImpl favouriteService;

    @Test
    void addFavourite_Success() throws ExecutionException, InterruptedException {
        CatalogItemRequest item = new CatalogItemRequest("CLINIC", "link");

        com.google.common.util.concurrent.ListenableFuture<Catalog.CatalogItemIdResponse> lf = mock(com.google.common.util.concurrent.ListenableFuture.class);
        when(stub.getCatalogItemId(any())).thenReturn(lf);
        Catalog.CatalogItemIdResponse resp = Catalog.CatalogItemIdResponse.newBuilder().setId(10L).build();
        when(lf.get()).thenReturn(resp);

        User user = new User();
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        when(repo.save(any(Favourite.class))).thenReturn(new Favourite());

        CompletableFuture<Void> future = favouriteService.addFavourite(1L, item);
        future.get();

        verify(repo).save(any(Favourite.class));
    }

    @Test
    void deleteFavourite_Success() throws ExecutionException, InterruptedException {
        CatalogItemRequest item = new CatalogItemRequest("CLINIC", "link");

        com.google.common.util.concurrent.ListenableFuture<Catalog.CatalogItemIdResponse> lf = mock(com.google.common.util.concurrent.ListenableFuture.class);
        when(stub.getCatalogItemId(any())).thenReturn(lf);
        Catalog.CatalogItemIdResponse resp = Catalog.CatalogItemIdResponse.newBuilder().setId(10L).build();
        when(lf.get()).thenReturn(resp);

        CompletableFuture<Void> future = favouriteService.deleteFavourite(1L, item);
        future.get();

        verify(repo).deleteById(any(FavouriteId.class));
    }

    @Test
    void getFavourites_Success() throws ExecutionException, InterruptedException {
        Favourite fav = new Favourite();
        FavouriteId id = new FavouriteId(1L, 10L, "CLINIC");
        fav.setId(id);
        when(repo.findByUserId(1L)).thenReturn(List.of(fav));

        com.google.common.util.concurrent.ListenableFuture<Catalog.CatalogItemsResponse> lf = mock(com.google.common.util.concurrent.ListenableFuture.class);
        when(stub.getCatalogItems(any())).thenReturn(lf);
        Catalog.CatalogItem catalogItem = Catalog.CatalogItem.newBuilder().setId(10L).setType("CLINIC").build();
        Catalog.CatalogItemsResponse resp = Catalog.CatalogItemsResponse.newBuilder().addItems(catalogItem).build();
        when(lf.get()).thenReturn(resp);

        CatalogItemDto dto = CatalogItemDto.builder().build();
        dto.setType("CLINIC");
        when(mapper.toDto(catalogItem)).thenReturn(dto);

        CompletableFuture<Map<String, List<CatalogItemDto>>> future = favouriteService.getFavourites(1L);
        Map<String, List<CatalogItemDto>> result = future.get();

        assertNotNull(result);
        assertTrue(result.containsKey("CLINIC"));
        assertEquals(1, result.get("CLINIC").size());
    }
}