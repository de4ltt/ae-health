package feo.health.catalog_service.service.catalog_item_provider;

import catalog.Catalog;
import feo.health.catalog_service.mapper.DoctorMapper;
import feo.health.catalog_service.model.entity.Doctor;
import feo.health.catalog_service.repository.DoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public List<Catalog.CatalogItem> getCatalogItems(List<Long> ids) {
        return doctorMapper.toCatalogItem(doctorRepository.findAllById(ids));
    }

    @Override
    public Long getCatalogItemId(String link) {
        Optional<Doctor> clinic = doctorRepository.findByLink(link);
        return clinic.map(Doctor::getId).orElse(null);
    }
}
