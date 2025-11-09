package feo.health.catalog_service.service.catalog_item_provider;

import catalog.Catalog;
import feo.health.catalog_service.mapper.ClinicMapper;
import feo.health.catalog_service.model.entity.Clinic;
import feo.health.catalog_service.repository.ClinicRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
    @Async
    public CompletableFuture<List<Catalog.CatalogItem>> getCatalogItems(List<Long> ids) {
        return CompletableFuture.supplyAsync(() -> {
            List<Clinic> clinics = clinicRepository.findAllById(ids);
            return clinicMapper.toCatalogItem(clinics);
        });
    }

    @Override
    @Async
    public CompletableFuture<Long> getCatalogItemId(String link) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Clinic> clinic = clinicRepository.findByLink(link);
            return clinic.map(Clinic::getId).orElse(null);
        });
    }
}