package feo.health.catalog_service.service.db.clinic;

import feo.health.catalog_service.model.entity.Clinic;
import feo.health.catalog_service.repository.ClinicRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ClinicDatabaseServiceImpl implements ClinicDatabaseService {

    private ClinicRepository clinicRepository;

    @Override
    public Clinic saveClinic(Clinic clinic) {
        return clinicRepository.save(clinic);
    }

    @Override
    public Optional<Clinic> getClinicByLink(String link) {
        return clinicRepository.findByLink(link);
    }
}