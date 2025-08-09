package feo.health.catalog_service.service.clinic;

import feo.health.catalog_service.model.dto.ClinicDto;

import java.util.List;

public interface ClinicService {
    List<ClinicDto> searchClinics(String query, Boolean located);
    List<ClinicDto> getClinicsByType(String uri);
    List<ClinicDto> getClinicsByService(String uri);
    ClinicDto getClinicInfo(String query, Boolean located);
}
