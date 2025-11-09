package feo.health.catalog_service.service.general_search;

import feo.health.catalog_service.model.dto.SearchDto;

import java.util.concurrent.CompletableFuture;

public interface GeneralSearchService {
    CompletableFuture<SearchDto> search(String query, Boolean located);
}