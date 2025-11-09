package feo.health.catalog_service.service.clinic;

import feo.health.catalog_service.model.dto.ClinicDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ClinicService {
    CompletableFuture<List<ClinicDto>> searchClinics(String query, Boolean located);
    CompletableFuture<List<ClinicDto>> getClinicsByType(String uri);
    CompletableFuture<List<ClinicDto>> getClinicsByService(String uri);
    CompletableFuture<ClinicDto> getClinicInfo(String query, Boolean located, Long userId);
}