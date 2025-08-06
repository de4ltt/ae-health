package feo.health.catalog_service.service;

import feo.health.catalog_service.entity.Clinic;

import java.util.Optional;

public interface ClinicDatabaseService {
    boolean isClinicPresentByUri(String uri);
    Clinic saveClinic(Clinic clinic);
    Optional<Clinic> getClinicByUri(String uri);
}
