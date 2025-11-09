package feo.health.catalog_service.mapper;

import catalog.Catalog;
import feo.health.catalog_service.model.dto.ClinicDto;
import feo.health.catalog_service.model.entity.Clinic;
import feo.health.catalog_service.repository.ClinicRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import user.User;

import java.util.List;

@Component
@AllArgsConstructor
public class ClinicMapper {

    private ClinicRepository clinicRepository;

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
        return clinicRepository.findByLink(clinicDto.getLink())
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

    public User.SaveToHistoryRequest toHistoryRequest(Clinic clinic, Long userId) {
        return User.SaveToHistoryRequest.newBuilder()
                .setItemId(clinic.getId())
                .setItemType("clinic")
                .setUserId(userId)
                .build();
    }

    public Catalog.CatalogItem toCatalogItem(Clinic clinic) {
        return Catalog.CatalogItem.newBuilder()
                .setName(clinic.getName())
                .setLink(clinic.getLink())
                .setImageUri(clinic.getImageUri())
                .setType("clinic")
                .setId(clinic.getId())
                .build();
    }

    public List<Catalog.CatalogItem> toCatalogItem(List<Clinic> clinics) {
        return clinics.stream().map(this::toCatalogItem).toList();
    }
}
