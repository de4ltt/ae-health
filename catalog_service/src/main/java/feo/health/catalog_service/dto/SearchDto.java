package feo.health.catalog_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchDto {

    private List<DoctorDto> doctors;
    private List<ClinicDto> clinics;
    private List<ServiceDto> services;

}
