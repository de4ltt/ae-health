package feo.health.catalog_service.service.impl;

import feo.health.catalog_service.entity.Clinic;
import feo.health.catalog_service.repository.ClinicRepository;
import feo.health.catalog_service.service.ClinicDatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ClinicDatabaseServiceImpl implements ClinicDatabaseService {

    private ClinicRepository clinicRepository;

    @Override
    public boolean isClinicPresentByUri(String uri) {
        return clinicRepository.existsByUri(uri);
    }

    @Override
    public Clinic saveClinic(Clinic clinic) {
        return clinicRepository.save(clinic);
    }

    @Override
    public Optional<Clinic> getClinicByUri(String uri) {
        return clinicRepository.findByUri(uri);
    }

}
