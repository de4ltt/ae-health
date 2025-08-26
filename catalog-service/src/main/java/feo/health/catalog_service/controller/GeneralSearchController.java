package feo.health.catalog_service.controller;

import feo.health.catalog_service.model.dto.SearchDto;
import feo.health.catalog_service.service.general_search.GeneralSearchService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/catalog/search")
@AllArgsConstructor
public class GeneralSearchController {

    private final GeneralSearchService generalSearchService;

    @GetMapping
    public ResponseEntity<SearchDto> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "true") Boolean located
    ) {
        return ResponseEntity.ok(generalSearchService.search(q, located));
    }
}
