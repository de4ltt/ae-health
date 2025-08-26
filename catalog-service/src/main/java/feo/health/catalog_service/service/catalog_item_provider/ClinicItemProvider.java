package feo.health.catalog_service.service.catalog_item_provider;

import catalog.Catalog;
import feo.health.catalog_service.mapper.ClinicMapper;
import feo.health.catalog_service.model.entity.Clinic;
import feo.health.catalog_service.repository.ClinicRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClinicItemProvider implements CatalogItemProvider {

    private final ClinicRepository clinicRepository;
    private final ClinicMapper clinicMapper;

    @Override
    public String getType() {
        return "clinic";
    }

    @Override
    public List<Catalog.CatalogItem> getCatalogItems(List<Long> ids) {
        return clinicMapper.toCatalogItem(clinicRepository.findAllById(ids));
    }

    @Override
    public Long getCatalogItemId(String link) {
        Optional<Clinic> clinic = clinicRepository.findByLink(link);
        return clinic.map(Clinic::getId).orElse(null);
    }
}
