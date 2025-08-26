package feo.health.catalog_service.controller;

import feo.health.catalog_service.model.dto.DiseaseDto;
import feo.health.catalog_service.service.disease.DiseaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog/diseases")
@AllArgsConstructor
public class DiseaseController {

    private final DiseaseService diseaseService;

    @GetMapping
    public ResponseEntity<List<DiseaseDto>> searchDiseases(@RequestParam String q) {
        return ResponseEntity.ok(diseaseService.searchDiseases(q));
    }

    @GetMapping("/{uri}")
    public ResponseEntity<String> getDiseaseInfo(@PathVariable String uri) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.TEXT_HTML)
                .body(diseaseService.getDiseaseArticle(uri));
    }
}
