package feo.health.catalog_service.mapper;

import feo.health.catalog_service.dto.OverpassPharmaciesResponse;
import feo.health.catalog_service.dto.PharmacyDto;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class PharmacyMapper {

    public List<PharmacyDto> toDtoFromOverpassDto(OverpassPharmaciesResponse response) {
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
                    dto.setOpeningHours(transformOpeningHours(tags.getOrDefault("opening_hours", null)));

                    return dto;
                })
                .toList();
    }

    private List<String> transformOpeningHours(String openingHours) {
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
