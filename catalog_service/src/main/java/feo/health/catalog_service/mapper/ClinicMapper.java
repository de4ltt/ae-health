package feo.health.catalog_service.mapper;

import feo.health.catalog_service.dto.ClinicDto;
import feo.health.catalog_service.entity.Clinic;
import feo.health.catalog_service.service.ClinicDatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ClinicMapper {

    private ClinicDatabaseService clinicDatabaseService;

    public ClinicDto toDto(Clinic clinic) {

        if (clinic == null) return null;

        ClinicDto dto = new ClinicDto();
        dto.setName(clinic.getName());
        dto.setAddress(clinic.getAddress());
        dto.setUri(clinic.getUri());

        return dto;
    }

    public Clinic toEntity(ClinicDto clinicDto) {
        return clinicDatabaseService.getClinicByUri(clinicDto.getUri())
                .orElseGet(() -> {
                    Clinic clinic = new Clinic();
                    clinic.setName(clinicDto.getName());
                    clinic.setUri(clinicDto.getUri());
                    clinic.setAddress(clinicDto.getAddress());
                    clinic.setPhoneNumber(clinicDto.getPhoneNumber());
                    clinic.setImageUri(clinicDto.getImageUri());
                    return clinicDatabaseService.saveClinic(clinic);
                });
    }

    public List<Clinic> toEntity(List<ClinicDto> clinicDtos) {
        return clinicDtos.stream().map(this::toEntity).toList();
    }

    public List<ClinicDto> toDto(List<Clinic> clinics) {
        return clinics.stream().map(this::toDto).toList();
    }

}
