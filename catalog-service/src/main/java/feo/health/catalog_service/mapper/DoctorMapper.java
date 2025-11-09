package feo.health.catalog_service.mapper;

import catalog.Catalog;
import feo.health.catalog_service.model.dto.DoctorDto;
import feo.health.catalog_service.model.entity.Doctor;
import feo.health.catalog_service.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import user.User;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DoctorMapper {

    private final SpecialityMapper specialityMapper;
    private final ReviewMapper reviewMapper;
    private final DoctorsServiceMapper doctorsServiceMapper;

    private final DoctorRepository doctorRepository;

    public DoctorDto toDto(Doctor doctor) {

        if (doctor == null) return null;

        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setName(doctor.getName());
        doctorDto.setLink(doctor.getLink());
        doctorDto.setImageUri(doctor.getImageUri());
        doctorDto.setItemType("doctor");
        doctorDto.setExperience(doctor.getExperience());
        doctorDto.setSpecialities(specialityMapper.toDto(doctor.getSpecialities()));
        doctorDto.setReviews(reviewMapper.toDto(doctor.getReviews()));
        doctorDto.setServices(doctorsServiceMapper.toDto(doctor.getServices()));
        doctorDto.setRating(doctor.getRating());

        return doctorDto;
    }

    public User.SaveToHistoryRequest toHistoryRequest(Doctor doctor, Long userId) {
        return User.SaveToHistoryRequest.newBuilder()
                .setItemId(doctor.getId())
                .setItemType("doctor")
                .setUserId(userId)
                .build();
    }

    public Doctor toEntity(DoctorDto doctorDto) {

        if (doctorDto == null) return null;

        Optional<Doctor> doctorOptional = doctorRepository.findByLink(doctorDto.getLink());

        if (doctorOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();
            doctor.setRating(doctor.getRating() == null ? doctorDto.getRating() : doctor.getRating());
            doctorRepository.save(doctor);
            return doctor;
        }

        Doctor doctor = new Doctor();
        doctor.setName(doctorDto.getName());
        doctor.setLink(doctorDto.getLink());
        doctor.setExperience(doctorDto.getExperience());
        doctor.setImageUri(doctorDto.getImageUri());
        doctor.setSpecialities(specialityMapper.toEntity(doctorDto.getSpecialities()));
        doctor.setReviews(reviewMapper.toEntity(doctorDto.getReviews(), doctor));
        doctor.setServices(doctorsServiceMapper.toEntity(doctorDto.getServices(), doctor));
        doctor.setRating(doctorDto.getRating());

        return doctor;
    }

    public Catalog.CatalogItem toCatalogItem(Doctor doctor) {
        return Catalog.CatalogItem.newBuilder()
                .setName(doctor.getName())
                .setLink(doctor.getLink())
                .setImageUri(doctor.getImageUri())
                .setType("doctor")
                .setId(doctor.getId())
                .build();
    }

    public List<Catalog.CatalogItem> toCatalogItem(List<Doctor> doctors) {
        return doctors.stream().map(this::toCatalogItem).toList();
    }
}