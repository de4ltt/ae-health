package feo.health.catalog_service.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class DoctorDto {

    private String name;

    @NotNull
    private String link;

    @NotNull
    private List<SpecialityDto> specialities;

    private Byte experience;

    private String imageUri;

    private Float rating;

    @NotNull
    private String itemType;

    private List<ReviewDto> reviews;

    private List<DoctorsServiceDto> services;

    private List<ClinicDto> clinics;

    public static Float calculateDoctorRating(List<ReviewDto> reviewDtos) {
        List<Float> ratings = reviewDtos.stream().map(ReviewDto::getRating).toList();

        Integer count = ratings.size();
        Float sum = 0f;

        for (Float rating : ratings)
            sum += rating;

        return sum / count;
    }

    public static String clearDoctorLink(String link) {
        String result = link
                .replace("https://prodoctorov.ru/", "")
                .replace("krasnodar", "")
                .replace("vrach", "")
                .replaceAll("/", "");
        return result.contains("#") ? result.substring(0, result.indexOf("#")) : result;
    }
}
