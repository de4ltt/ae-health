package feo.health.catalog_service.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class PharmacyDto {

    private String name;

    private String phoneNumber;

    private String website;

    private String address;

    private List<String> openingHours;
}
