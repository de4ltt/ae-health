package feo.health.catalog_service.service.catalog_item_provider;

import catalog.Catalog;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CatalogItemProvider {
    String getType();
    CompletableFuture<List<Catalog.CatalogItem>> getCatalogItems(List<Long> ids);
    CompletableFuture<Long> getCatalogItemId(String link);
}