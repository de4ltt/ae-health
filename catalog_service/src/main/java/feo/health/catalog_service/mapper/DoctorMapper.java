package feo.health.catalog_service.mapper;

import feo.health.catalog_service.dto.*;
import feo.health.catalog_service.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DoctorMapper {

    private final ClinicMapper clinicMapper;
    private final SpecialityMapper specialityMapper;
    private final ReviewMapper reviewMapper;
    private final DoctorsServiceMapper doctorsServiceMapper;

    public DoctorDto toDto(Doctor doctor) {

        if (doctor == null) return null;

        DoctorDto dto = new DoctorDto();
        dto.setName(doctor.getName());
        dto.setUri(doctor.getUri());
        dto.setImageUri(doctor.getImageUri());
        dto.setItemType("doctor");
        dto.setExperience(doctor.getExperience());
        dto.setSpecialities(specialityMapper.toDto(doctor.getSpecialities()));
        dto.setReviews(reviewMapper.toDto(doctor.getReviews()));
        dto.setServices(doctorsServiceMapper.toDto(doctor.getServices()));
        dto.setClinics(clinicMapper.toDto(doctor.getClinics()));

        return dto;
    }

    public Doctor toEntity(DoctorDto doctorDto) {

        if (doctorDto == null) return null;

        Doctor doctor = new Doctor();
        doctor.setName(doctorDto.getName());
        doctor.setUri(doctorDto.getUri());
        doctor.setExperience(doctorDto.getExperience());
        doctor.setImageUri(doctorDto.getImageUri());
        doctor.setSpecialities(specialityMapper.toEntity(doctorDto.getSpecialities()));
        doctor.setReviews(reviewMapper.toEntity(doctorDto.getReviews(), doctor));
        doctor.setServices(doctorsServiceMapper.toEntity(doctorDto.getServices(), doctor));
        doctor.setClinics(clinicMapper.toEntity(doctorDto.getClinics()));

        return doctor;
    }

}