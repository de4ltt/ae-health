package feo.health.catalog_service.controller;

import feo.health.catalog_service.model.dto.PharmacyDto;
import feo.health.catalog_service.service.pharmacy.PharmacyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/catalog/pharmacies")
@AllArgsConstructor
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<PharmacyDto>>> searchPharmacies(
            @RequestParam Double lat,
            @RequestParam Double lon,
            @RequestParam(defaultValue = "500") Integer radius
    ) {
        return pharmacyService.searchPharmacies(radius, lat, lon)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/visit")
    public CompletableFuture<ResponseEntity<Void>> visitPharmacy(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody PharmacyDto pharmacyDto
    ) {
        return pharmacyService.visitPharmacy(pharmacyDto, userId)
                .thenApply(v -> ResponseEntity.ok().build());
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<PharmacyDto>> getPharmacyById(@PathVariable Long id) {
        return pharmacyService.getPharmacyById(id)
                .thenApply(ResponseEntity::ok);
    }
}