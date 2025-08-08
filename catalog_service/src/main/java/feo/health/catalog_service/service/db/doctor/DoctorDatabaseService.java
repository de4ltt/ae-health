package feo.health.catalog_service.service.db.doctor;

import feo.health.catalog_service.entity.Doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorDatabaseService {
    Doctor saveDoctor(Doctor doctor);
    List<Doctor> saveDoctors(List<Doctor> doctors);
    boolean isDoctorPresentByUrl(String url);
    Optional<Doctor> getDoctorByUrl(String url);
}
