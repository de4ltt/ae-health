package feo.health.catalog_service.mapper;

import feo.health.catalog_service.model.dto.ClinicDto;
import feo.health.catalog_service.model.entity.Clinic;
import feo.health.catalog_service.service.db.clinic.ClinicDatabaseService;
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
        dto.setLink(clinic.getLink());
        dto.setItemType("clinic");

        return dto;
    }

    public Clinic toEntity(ClinicDto clinicDto) {
        return clinicDatabaseService.getClinicByLink(clinicDto.getLink())
                .orElseGet(() -> {
                    Clinic clinic = new Clinic();
                    clinic.setName(clinicDto.getName());
                    clinic.setLink(clinicDto.getLink());
                    clinic.setAddress(clinicDto.getAddress());
                    clinic.setPhoneNumber(clinicDto.getPhoneNumber());
                    clinic.setImageUri(clinicDto.getImageUri());
                    return clinic;
                });
    }

    public List<ClinicDto> toDto(List<Clinic> clinics) {
        return clinics.stream().map(this::toDto).toList();
    }

    public List<Clinic> toEntity(List<ClinicDto> clinicDtos) {
        return clinicDtos.stream().map(this::toEntity).toList();
    }
}
