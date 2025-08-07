package feo.health.catalog_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class DoctorDto {

    private String name;

    @NotNull
    private String uri;

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

}
