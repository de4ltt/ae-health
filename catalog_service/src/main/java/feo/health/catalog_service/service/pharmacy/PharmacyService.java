package feo.health.catalog_service.service.pharmacy;

import feo.health.catalog_service.model.dto.PharmacyDto;

import java.util.List;

public interface PharmacyService {
    List<PharmacyDto> searchPharmacies(Integer radius, Double lat, Double lon);
}
