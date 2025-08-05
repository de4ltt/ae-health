package feo.health.catalog_service.service;

import feo.health.catalog_service.entity.Speciality;

import java.util.Optional;

public interface SpecialityDatabaseService {
    Speciality saveSpeciality(Speciality speciality);
    Optional<Speciality> findByNameIgnoreCase(String name);
}