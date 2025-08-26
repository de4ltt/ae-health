package feo.health.catalog_service.service.catalog;

import catalog.Catalog;
import catalog.CatalogServiceGrpc;
import feo.health.catalog_service.service.catalog_item_provider.CatalogItemProvider;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

@GrpcService
@AllArgsConstructor
public class CatalogServiceImpl extends CatalogServiceGrpc.CatalogServiceImplBase {

    private final List<CatalogItemProvider> catalogItemProviders;

    @Override
    public void getCatalogItemId(Catalog.CatalogItemIdRequest request, StreamObserver<Catalog.CatalogItemIdResponse> responseObserver) {

        String link = request.getLink();
        String type = request.getType();

        CatalogItemProvider provider = getProvider(type);

        Long id = provider.getCatalogItemId(link);

        Catalog.CatalogItemIdResponse response = Catalog.CatalogItemIdResponse.newBuilder()
                .setId(id)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getCatalogItems(Catalog.CatalogItemsRequest request, StreamObserver<Catalog.CatalogItemsResponse> responseObserver) {

        List<Long> idsList = request.getIdsList();
        String type = request.getType();

        CatalogItemProvider provider = getProvider(type);

        List<Catalog.CatalogItem> catalogItems = provider.getCatalogItems(idsList);

        Catalog.CatalogItemsResponse response = Catalog.CatalogItemsResponse.newBuilder()
                .addAllItems(catalogItems)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private CatalogItemProvider getProvider(String type) {
        return catalogItemProviders.stream()
                .filter(catalogItemProvider -> catalogItemProvider.getType().equals(type))
                .findFirst().orElseThrow(() -> new RuntimeException("Not implemented type: " + type));
    }
}
