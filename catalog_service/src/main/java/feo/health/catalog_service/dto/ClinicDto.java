package feo.health.catalog_service.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ClinicDto {

    @NotNull
    private String name;

    @NotNull
    private String uri;

    private String address;

    private String phoneNumber;

    private String imageUri;

    @NotNull
    private String itemType;

    private List<DoctorDto> doctors;

    private List<ReviewDto> reviews;

}
