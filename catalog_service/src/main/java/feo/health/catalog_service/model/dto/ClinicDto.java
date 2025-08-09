package feo.health.catalog_service.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.function.Function;

@Data
public class ClinicDto {

    @NotNull
    private String name;

    @NotNull
    private String link;

    private String address;

    private String phoneNumber;

    private String imageUri;

    @NotNull
    private String itemType;

    private List<ReviewDto> reviews;

    public static List<ClinicDto> removeLocationFromNames(List<ClinicDto> clinicDtos) {

        Function<ClinicDto, ClinicDto> removeLocationFromName = clinicDto -> {
            if (clinicDto.getName().contains(" на "))
                clinicDto.setName(clinicDto.getName().substring(0, clinicDto.getName().indexOf(" на ")).trim());
            return clinicDto;
        };

        return clinicDtos.stream().map(removeLocationFromName).toList();
    }

    public static String clearClinicLink(String link) {
        return link
                .replace("https://prodoctorov.ru/", "")
                .replace("krasnodar", "")
                .replace("lpu", "")
                .replace("top", "")
                .replaceAll("/", "");
    }
}
