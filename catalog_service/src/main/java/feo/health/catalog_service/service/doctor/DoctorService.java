package feo.health.catalog_service.service.doctor;

import feo.health.catalog_service.model.dto.DoctorDto;

import java.util.List;

public interface DoctorService {
    List<DoctorDto> searchDoctors(String query);
    DoctorDto getDoctorInfo(String doctorUri);
    List<DoctorDto> getDoctorsBySpeciality(String specialityUri);
    List<DoctorDto> getClinicDoctors(String clinicUri);
}
