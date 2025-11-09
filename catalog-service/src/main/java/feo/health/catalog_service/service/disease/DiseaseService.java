package feo.health.catalog_service.service.disease;

import feo.health.catalog_service.model.dto.DiseaseDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DiseaseService {
    CompletableFuture<String> getDiseaseArticle(String uri);
    CompletableFuture<List<DiseaseDto>> searchDiseases(String query);
}