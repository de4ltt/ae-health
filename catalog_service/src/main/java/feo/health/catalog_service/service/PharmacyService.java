package feo.health.catalog_service.service;

import feo.health.catalog_service.dto.PharmacyDto;

import java.util.List;

public interface PharmacyService {
    List<PharmacyDto> searchPharmacies(Integer radius, Double lat, Double lon);
}
