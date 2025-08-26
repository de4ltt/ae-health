package feo.health.catalog_service.service.catalog_item_provider;

import catalog.Catalog;

import java.util.List;

public interface CatalogItemProvider {
    String getType();
    List<Catalog.CatalogItem> getCatalogItems(List<Long> ids);
    Long getCatalogItemId(String link);
}
