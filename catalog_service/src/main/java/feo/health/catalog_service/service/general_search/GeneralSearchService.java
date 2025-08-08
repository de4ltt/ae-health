package feo.health.catalog_service.service.general_search;

import feo.health.catalog_service.dto.SearchDto;

public interface GeneralSearchService {
    SearchDto search(String query, Boolean located);
}
