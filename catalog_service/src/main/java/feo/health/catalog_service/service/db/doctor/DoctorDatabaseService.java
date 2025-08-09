package feo.health.catalog_service.service.db.doctor;

import feo.health.catalog_service.model.entity.Doctor;

import java.util.Optional;

public interface DoctorDatabaseService {
    Doctor saveDoctor(Doctor doctor);
    Optional<Doctor> getDoctorByUrl(String url);
}
