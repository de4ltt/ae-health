package feo.health.catalog_service.controller;

import feo.health.catalog_service.dto.DrugDto;
import feo.health.catalog_service.service.drug.DrugService;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog/drugs")
@AllArgsConstructor
public class DrugsController {

    final DrugService drugService;

    @GetMapping
    public ResponseEntity<List<DrugDto>> searchDrugs(@RequestParam String query) {
        return ResponseEntity.ok(drugService.searchDrugs(query));
    }

    @GetMapping("/{uri}")
    public ResponseEntity<DrugDto> getDrugInfo(@PathVariable String uri) {
        return ResponseEntity.ok(drugService.getDrugInfo(uri));
    }

}
