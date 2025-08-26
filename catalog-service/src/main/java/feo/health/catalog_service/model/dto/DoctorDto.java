package feo.health.catalog_service.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

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

    public static Float calculateDoctorRating(List<ReviewDto> reviewDtos) {

        List<Float> ratings = reviewDtos.stream().map(ReviewDto::getRating).toList();

        double sum = ratings.stream().mapToDouble(Float::doubleValue).sum();
        int count = ratings.size();

        return (float) (sum / count);
    }

    public static String clearDoctorLink(String link) {
        String result = link
                .replace("https://prodoctorov.ru/", "")
                .replace("krasnodar", "")
                .replaceFirst("vrach", "")
                .replaceAll("/", "");
        return result.contains("#") ? result.substring(0, result.indexOf("#")) : result;
    }
}
