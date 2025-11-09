package feo.health.catalog_service.mapper;

import catalog.Catalog;
import feo.health.catalog_service.model.dto.OverpassPharmaciesResponse;
import feo.health.catalog_service.model.dto.PharmacyDto;
import feo.health.catalog_service.model.entity.Pharmacy;
import feo.health.catalog_service.repository.PharmacyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import user.User;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
public class PharmacyMapper {

    private final PharmacyRepository pharmacyRepository;

    public static List<PharmacyDto> toDtoFromOverpassDto(OverpassPharmaciesResponse response) {
        if (response == null || response.getElements() == null) return List.of();

        return response.getElements().stream()
                .map(element -> {
                    Map<String, String> tags = element.getTags();
                    PharmacyDto dto = new PharmacyDto();

                    dto.setName(tags.getOrDefault("name", null));
                    dto.setPhoneNumber(tags.getOrDefault("phone", null));
                    dto.setWebsite(tags.getOrDefault("website", null));
                    dto.setAddress(tags.getOrDefault("addr:full",
                            tags.getOrDefault("addr:street", "") + " " +
                                    tags.getOrDefault("addr:housenumber", "")));
                    dto.setOpeningHours(translateOpeningHours(tags.getOrDefault("opening_hours", null)));

                    return dto;
                })
                .toList();
    }

    public User.SaveToHistoryRequest toHistoryRequest(Pharmacy pharmacy, Long userId) {
        return User.SaveToHistoryRequest.newBuilder()
                .setItemId(pharmacy.getId())
                .setItemType("pharmacy")
                .setUserId(userId)
                .build();
    }

    public Pharmacy toEntity(PharmacyDto pharmacyDto) {

        Optional<Pharmacy> pharmacy = pharmacyRepository
                .findByAddressAndName(pharmacyDto.getAddress(), pharmacyDto.getName());

        return pharmacy.orElseGet(() -> {
            Pharmacy pharmacyNew = new Pharmacy();
            pharmacyNew.setName(pharmacyDto.getName());
            pharmacyNew.setAddress(pharmacyDto.getAddress());
            pharmacyNew.setWebsite(pharmacyDto.getWebsite());
            pharmacyNew.setPhoneNumber(pharmacyDto.getPhoneNumber());
            pharmacyNew.setOpeningHours(String.join(";", pharmacyDto.getOpeningHours()));

            return pharmacyNew;
        });
    }

    public PharmacyDto toDto(Pharmacy pharmacy) {

        PharmacyDto pharmacyDto = new PharmacyDto();

        pharmacyDto.setName(pharmacy.getName());
        pharmacyDto.setAddress(pharmacy.getAddress());
        pharmacyDto.setWebsite(pharmacy.getWebsite());
        pharmacyDto.setPhoneNumber(pharmacy.getPhoneNumber());
        pharmacyDto.setOpeningHours(List.of(pharmacy.getOpeningHours().split(";")));

        return pharmacyDto;
    }

    public Catalog.CatalogItem toCatalogItem(Pharmacy pharmacy) {
        return Catalog.CatalogItem.newBuilder()
                .setType("pharmacy")
                .setLink(String.valueOf(pharmacy.getId()))
                .setName(pharmacy.getName())
                .setId(pharmacy.getId())
                .build();
    }

    public List<Catalog.CatalogItem> toCatalogItem(List<Pharmacy> pharmacies) {
        return pharmacies.stream().map(this::toCatalogItem).toList();
    }

    private static List<String> translateOpeningHours(String openingHours) {
        if (openingHours == null) return null;
        return Arrays.stream(openingHours
                .replaceAll("Mo", "Пн")
                .replaceAll("Tu", "Вт")
                .replaceAll("We", "Ср")
                .replaceAll("Th", "Чт")
                .replaceAll("Fr", "Пт")
                .replaceAll("Sa", "Сб")
                .replaceAll("Su", "Вс")
                .split("; ")
        ).toList();
    }
}
