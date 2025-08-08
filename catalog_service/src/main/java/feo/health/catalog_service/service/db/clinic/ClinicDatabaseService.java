package feo.health.catalog_service.service.db.clinic;

import feo.health.catalog_service.entity.Clinic;

import java.util.List;
import java.util.Optional;

public interface ClinicDatabaseService {
    boolean isClinicPresentByUri(String uri);
    Clinic saveClinic(Clinic clinic);
    List<Clinic> saveClinics(List<Clinic> clinics);
    Optional<Clinic> getClinicByUri(String uri);
}
