package feo.health.catalog_service.controller;

import feo.health.catalog_service.model.dto.PharmacyDto;
import feo.health.catalog_service.service.pharmacy.PharmacyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog/pharmacies")
@AllArgsConstructor
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @GetMapping
    public ResponseEntity<List<PharmacyDto>> searchPharmacies(
            @RequestParam Double lat,
            @RequestParam Double lon,
            @RequestParam(defaultValue = "500") Integer radius
    ) {
        return ResponseEntity.ok(pharmacyService.searchPharmacies(radius, lat, lon));
    }

    @PostMapping("/visit")
    public ResponseEntity<Void> visitPharmacy(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody PharmacyDto pharmacyDto
    ) {
        pharmacyService.visitPharmacy(pharmacyDto, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PharmacyDto> getPharmacyById(
            @PathVariable Long id
    ) { return ResponseEntity.ok(pharmacyService.getPharmacyById(id)); }
}

