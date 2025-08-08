package feo.health.catalog_service.mapper;

import feo.health.catalog_service.dto.DoctorDto;
import feo.health.catalog_service.entity.Doctor;
import feo.health.catalog_service.service.DoctorDatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DoctorMapper {

    private final ClinicMapper clinicMapper;
    private final SpecialityMapper specialityMapper;
    private final ReviewMapper reviewMapper;
    private final DoctorsServiceMapper doctorsServiceMapper;

    private final DoctorDatabaseService doctorDatabaseService;

    public DoctorDto toDto(Doctor doctor) {

        if (doctor == null) return null;

        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setName(doctor.getName());
        doctorDto.setUri(doctor.getUri());
        doctorDto.setImageUri(doctor.getImageUri());
        doctorDto.setItemType("doctor");
        doctorDto.setExperience(doctor.getExperience());
        doctorDto.setSpecialities(specialityMapper.toDto(doctor.getSpecialities()));
        doctorDto.setReviews(reviewMapper.toDto(doctor.getReviews()));
        doctorDto.setServices(doctorsServiceMapper.toDto(doctor.getServices()));
        doctorDto.setClinics(clinicMapper.toDto(doctor.getClinics()));
        doctorDto.setRating(doctor.getRating());

        return doctorDto;
    }

    public Doctor toEntity(DoctorDto doctorDto) {

        if (doctorDto == null) return null;

        if (doctorDatabaseService.isDoctorPresentByUrl(doctorDto.getUri())) {
            Doctor doctor = doctorDatabaseService.getDoctorByUrl(doctorDto.getUri()).get();
            doctor.setRating(doctor.getRating() == null ? doctorDto.getRating() : doctor.getRating());
            doctorDatabaseService.saveDoctor(doctor);
            return doctor;
        }

        Doctor doctor = new Doctor();
        doctor.setName(doctorDto.getName());
        doctor.setUri(doctorDto.getUri());
        doctor.setExperience(doctorDto.getExperience());
        doctor.setImageUri(doctorDto.getImageUri());
        doctor.setSpecialities(specialityMapper.toEntity(doctorDto.getSpecialities()));
        doctor.setReviews(reviewMapper.toEntity(doctorDto.getReviews(), doctor));
        doctor.setServices(doctorsServiceMapper.toEntity(doctorDto.getServices(), doctor));
        doctor.setClinics(clinicMapper.toEntity(doctorDto.getClinics()));
        doctor.setRating(doctorDto.getRating());

        return doctor;
    }

    public List<Doctor> toEntity(List<DoctorDto> doctorDtos) {
        return doctorDtos.stream()
                .map(this::toEntity)
                .filter(doctor -> doctor.getUri().contains("vrach"))
                .toList();
    }

}