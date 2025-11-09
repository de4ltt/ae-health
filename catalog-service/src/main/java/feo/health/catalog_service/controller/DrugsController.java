package feo.health.catalog_service.controller;

import feo.health.catalog_service.model.dto.DrugDto;
import feo.health.catalog_service.service.drug.DrugService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/catalog/drugs")
@AllArgsConstructor
public class DrugsController {

    final DrugService drugService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<DrugDto>>> searchDrugs(@RequestParam String q) {
        return drugService.searchDrugs(q)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{uri}")
    public CompletableFuture<ResponseEntity<DrugDto>> getDrugInfo(@PathVariable String uri) {
        return drugService.getDrugInfo(uri)
                .thenApply(ResponseEntity::ok);
    }
}