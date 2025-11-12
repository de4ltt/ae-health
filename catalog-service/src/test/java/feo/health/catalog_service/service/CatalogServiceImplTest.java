package feo.health.catalog_service.service;

import catalog.Catalog;
import feo.health.catalog_service.service.catalog.CatalogServiceImpl;
import feo.health.catalog_service.service.catalog_item_provider.CatalogItemProvider;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogServiceImplTest {

    @Mock
    private CatalogItemProvider provider;

    private CatalogServiceImpl catalogService;

    @BeforeEach
    void setUp() {
        catalogService = new CatalogServiceImpl(Collections.singletonList(provider));
    }

    @Test
    void getCatalogItemId_success() {
        Catalog.CatalogItemIdRequest request = Catalog.CatalogItemIdRequest.newBuilder()
                .setLink("/item")
                .setType("test")
                .build();
        StreamObserver<Catalog.CatalogItemIdResponse> observer = mock(StreamObserver.class);

        when(provider.getType()).thenReturn("test");
        when(provider.getCatalogItemId("/item")).thenReturn(CompletableFuture.completedFuture(1L));

        catalogService.getCatalogItemId(request, observer);

        verify(observer).onNext(any());
        verify(observer).onCompleted();
    }

    @Test
    void getCatalogItemId_error() {
        Catalog.CatalogItemIdRequest request = Catalog.CatalogItemIdRequest.newBuilder()
                .setLink("/item")
                .setType("test")
                .build();
        StreamObserver<Catalog.CatalogItemIdResponse> observer = mock(StreamObserver.class);

        when(provider.getType()).thenReturn("test");
        when(provider.getCatalogItemId("/item")).thenReturn(CompletableFuture.completedFuture(null));

        catalogService.getCatalogItemId(request, observer);

        verify(observer).onError(any());
    }

    @Test
    void getCatalogItems_success() {
        Catalog.CatalogItemsRequest request = Catalog.CatalogItemsRequest.newBuilder()
                .addIds(1L)
                .setType("test")
                .build();
        StreamObserver<Catalog.CatalogItemsResponse> observer = mock(StreamObserver.class);

        when(provider.getType()).thenReturn("test");
        when(provider.getCatalogItems(any())).thenReturn(CompletableFuture.completedFuture(Collections.emptyList()));

        catalogService.getCatalogItems(request, observer);

        verify(observer).onNext(any());
        verify(observer).onCompleted();
    }
}