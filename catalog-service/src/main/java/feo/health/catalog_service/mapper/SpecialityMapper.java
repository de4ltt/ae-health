package feo.health.catalog_service.mapper;

import feo.health.catalog_service.model.dto.SpecialityDto;
import feo.health.catalog_service.model.entity.Speciality;
import feo.health.catalog_service.repository.SpecialityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class SpecialityMapper {

    private SpecialityRepository specialityRepository;

    public Speciality toEntity(SpecialityDto specialityDto) {
        return specialityRepository.findByNameIgnoreCase(specialityDto.getName())
                .orElseGet(() -> {
                    Speciality speciality = new Speciality();
                    speciality.setLink(specialityDto.getLink());
                    speciality.setName(specialityDto.getName());
                    return specialityRepository.save(speciality);
                });
    }

    public SpecialityDto toDto(Speciality speciality) {
        if (speciality == null) return null;
        SpecialityDto specialityDto = new SpecialityDto();
        specialityDto.setName(speciality.getName().toLowerCase());
        specialityDto.setLink(speciality.getLink());
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
