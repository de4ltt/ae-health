package feo.health.catalog_service.service.drug;

import feo.health.catalog_service.model.dto.DrugDto;

import java.util.List;

public interface DrugService {
    List<DrugDto> searchDrugs(String query);
    DrugDto getDrugInfo(String drugUri);
}
