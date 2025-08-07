package feo.health.catalog_service.mapper;

import feo.health.catalog_service.dto.SpecialityDto;
import feo.health.catalog_service.entity.Speciality;
import feo.health.catalog_service.service.SpecialityDatabaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class SpecialityMapper {

    private SpecialityDatabaseService specialityDatabaseService;

    public Speciality toEntity(SpecialityDto specialityDto) {
        return specialityDatabaseService.findByNameIgnoreCase(specialityDto.getName())
                .orElseGet(() -> {
                    Speciality speciality = new Speciality();
                    speciality.setUri(specialityDto.getUri());
                    speciality.setName(specialityDto.getName());
                    return specialityDatabaseService.saveSpeciality(speciality);
                });
    }

    public SpecialityDto toDto(Speciality speciality) {
        if (speciality == null) return null;
        SpecialityDto specialityDto = new SpecialityDto();
        specialityDto.setName(speciality.getName().toLowerCase());
        specialityDto.setUri(speciality.getUri());
        return specialityDto;
    }

    public List<Speciality> toEntity(List<SpecialityDto> specialityDtos) {
        if (specialityDtos == null) return List.of();
        return specialityDtos.stream().map(this::toEntity).toList();
    }

    public List<SpecialityDto> toDto(List<Speciality> specialities) {
        if (specialities == null) return List.of();
        return specialities.stream().map(this::toDto).toList();
    }

}
