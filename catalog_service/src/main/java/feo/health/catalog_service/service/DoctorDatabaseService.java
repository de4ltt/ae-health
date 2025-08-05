package feo.health.catalog_service.service;

import feo.health.catalog_service.dto.DoctorDto;
import feo.health.catalog_service.dto.DoctorsServiceDto;
import feo.health.catalog_service.dto.ReviewDto;
import feo.health.catalog_service.dto.SpecialityDto;
import feo.health.catalog_service.entity.Doctor;

import java.util.Optional;

public interface DoctorDatabaseService {
    Doctor saveDoctor(Doctor doctor);
    boolean isDoctorPresentByUrl(String url);
    Optional<Doctor> getDoctorByUrl(String url);
}
