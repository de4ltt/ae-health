package feo.health.catalog_service.service.db.doctor_service;

import feo.health.catalog_service.entity.DoctorsService;

public interface DoctorsServiceDatabaseService {
    DoctorsService saveDoctorService(DoctorsService doctorsService);
}
