package feo.health.catalog_service.service.catalog_item_provider;

import catalog.Catalog;
import feo.health.catalog_service.mapper.PharmacyMapper;
import feo.health.catalog_service.model.entity.Pharmacy;
import feo.health.catalog_service.repository.PharmacyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PharmacyItemProvider implements CatalogItemProvider {

    private final PharmacyRepository pharmacyRepository;
    private final PharmacyMapper pharmacyMapper;

    @Override
    public String getType() {
        return "pharmacy";
    }

    @Override
    public List<Catalog.CatalogItem> getCatalogItems(List<Long> ids) {
        return pharmacyMapper.toCatalogItem(pharmacyRepository.findAllById(ids));
    }

    @Override
    public Long getCatalogItemId(String link) {
        Optional<Pharmacy> pharmacy = pharmacyRepository.findById(Long.valueOf(link));
        return pharmacy.map(Pharmacy::getId).orElse(null);
    }
}
