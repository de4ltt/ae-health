package feo.health.catalog_service.service.catalog_item_provider;

import catalog.Catalog;
import feo.health.catalog_service.mapper.DoctorMapper;
import feo.health.catalog_service.model.entity.Doctor;
import feo.health.catalog_service.repository.DoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class DoctorItemProvider implements CatalogItemProvider {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    @Override
    public String getType() {
        return "doctor";
    }

    @Override
    @Async
    public CompletableFuture<List<Catalog.CatalogItem>> getCatalogItems(List<Long> ids) {
        return CompletableFuture.supplyAsync(() -> {
            List<Doctor> doctors = doctorRepository.findAllById(ids);
            return doctorMapper.toCatalogItem(doctors);
        });
    }

    @Override
    @Async
    public CompletableFuture<Long> getCatalogItemId(String link) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Doctor> doctor = doctorRepository.findByLink(link);
            return doctor.map(Doctor::getId).orElse(null);
        });
    }
}