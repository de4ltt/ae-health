package feo.health.catalog_service.service.disease;

import feo.health.catalog_service.model.dto.DiseaseDto;

import java.util.List;

public interface DiseaseService {
    String getDiseaseArticle(String uri);
    List<DiseaseDto> searchDiseases(String query);
}
