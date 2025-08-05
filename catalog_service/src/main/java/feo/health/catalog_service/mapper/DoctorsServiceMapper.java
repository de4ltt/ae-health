package feo.health.catalog_service.mapper;

import feo.health.catalog_service.dto.DoctorsServiceDto;
import feo.health.catalog_service.entity.Doctor;
import feo.health.catalog_service.entity.DoctorsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class DoctorsServiceMapper {

    public DoctorsServiceDto toDto(DoctorsService doctorsService) {
        if (doctorsService == null) return null;
        DoctorsServiceDto doctorsServiceDto = new DoctorsServiceDto();
        doctorsServiceDto.setPrice(doctorsService.getPrice());
        doctorsServiceDto.setTitle(doctorsService.getTitle());
        return doctorsServiceDto;
    }

    public DoctorsService toEntity(DoctorsServiceDto dto, Doctor doctor) {
        DoctorsService entity = new DoctorsService();
        entity.setTitle(dto.getTitle());
        entity.setPrice(dto.getPrice());
        entity.setDoctor(doctor);
        return entity;
    }

    public List<DoctorsService> toEntity(List<DoctorsServiceDto> doctorsServiceDtos, Doctor doctor) {
        return doctorsServiceDtos.stream()
                .map(doctorsServiceDto -> this.toEntity(doctorsServiceDto, doctor)).toList();
    }

    public List<DoctorsServiceDto> toDto(List<DoctorsService> doctorServices) {
        return doctorServices.stream().map(this::toDto).toList();
    }

}
