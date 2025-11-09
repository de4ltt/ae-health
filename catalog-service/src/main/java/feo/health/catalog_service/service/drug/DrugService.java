package feo.health.catalog_service.service.drug;

import feo.health.catalog_service.model.dto.DrugDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DrugService {
    CompletableFuture<List<DrugDto>> searchDrugs(String query);
    CompletableFuture<DrugDto> getDrugInfo(String drugUri);
}