package feo.health.catalog_service.service.db.clinic;

import feo.health.catalog_service.model.entity.Clinic;

import java.util.Optional;

public interface ClinicDatabaseService {
    Clinic saveClinic(Clinic clinic);
    Optional<Clinic> getClinicByLink(String link);
}
