package feo.health.catalog_service.controller;

import feo.health.catalog_service.dto.PharmacyDto;
import feo.health.catalog_service.service.pharmacy.PharmacyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}

