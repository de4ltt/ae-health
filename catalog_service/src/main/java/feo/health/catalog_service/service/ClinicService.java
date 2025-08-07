package feo.health.catalog_service.service;

import feo.health.catalog_service.dto.ClinicDto;

import java.util.List;

public interface ClinicService {
    List<ClinicDto> searchClinics(String query, Boolean located);
    List<ClinicDto> getClinicsByService(String uri);
    ClinicDto getClinicInfo(String query, Boolean located);
}
