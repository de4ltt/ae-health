package feo.health.catalog_service.service.pharmacy;

import feo.health.catalog_service.model.dto.PharmacyDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PharmacyService {
    CompletableFuture<List<PharmacyDto>> searchPharmacies(Integer radius, Double lat, Double lon);
    CompletableFuture<Void> visitPharmacy(PharmacyDto pharmacyDto, Long userId);
    CompletableFuture<PharmacyDto> getPharmacyById(Long id);
}