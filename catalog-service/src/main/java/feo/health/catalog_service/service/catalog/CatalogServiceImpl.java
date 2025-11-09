package feo.health.catalog_service.service.catalog;

import catalog.Catalog;
import catalog.CatalogServiceGrpc;
import feo.health.catalog_service.service.catalog_item_provider.CatalogItemProvider;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@GrpcService
@AllArgsConstructor
public class CatalogServiceImpl extends CatalogServiceGrpc.CatalogServiceImplBase {

    private final List<CatalogItemProvider> catalogItemProviders;

    @Override
    public void getCatalogItemId(Catalog.CatalogItemIdRequest request,
                                 StreamObserver<Catalog.CatalogItemIdResponse> responseObserver) {

        String link = request.getLink();
        String type = request.getType();

        CatalogItemProvider provider = getProvider(type);

        provider.getCatalogItemId(link)
                .thenApply(id -> {
                    if (id == null)
                        throw new IllegalArgumentException("Catalog item not found for link: " + link);
                    return Catalog.CatalogItemIdResponse.newBuilder().setId(id).build();
                })
                .whenComplete((response, throwable) -> {
                    if (throwable != null) {
                        responseObserver.onError(throwable);
                    } else {
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                    }
                });
    }

    @Override
    public void getCatalogItems(Catalog.CatalogItemsRequest request,
                                StreamObserver<Catalog.CatalogItemsResponse> responseObserver) {

        List<Long> idsList = request.getIdsList();
        String type = request.getType();

        CatalogItemProvider provider = getProvider(type);

        provider.getCatalogItems(idsList)
                .thenApply(catalogItems -> Catalog.CatalogItemsResponse.newBuilder()
                        .addAllItems(catalogItems)
                        .build())
                .whenComplete((response, throwable) -> {
                    if (throwable != null) {
                        responseObserver.onError(throwable);
                    } else {
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                    }
                });
    }

    private CatalogItemProvider getProvider(String type) {
        return catalogItemProviders.stream()
                .filter(provider -> provider.getType().equals(type))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not implemented type: " + type));
    }
}