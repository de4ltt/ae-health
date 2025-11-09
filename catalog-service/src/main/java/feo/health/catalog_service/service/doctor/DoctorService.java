package feo.health.catalog_service.service.doctor;

import feo.health.catalog_service.model.dto.ClinicDto;
import feo.health.catalog_service.model.dto.DoctorDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DoctorService {
    CompletableFuture<List<DoctorDto>> searchDoctors(String query);
    CompletableFuture<DoctorDto> getDoctorInfo(String doctorUri, Long userId);
    CompletableFuture<List<DoctorDto>> getDoctorsBySpeciality(String specialityUri);
    CompletableFuture<List<DoctorDto>> getClinicDoctors(String clinicUri);
    CompletableFuture<List<ClinicDto>> getDoctorClinics(String uri);
}