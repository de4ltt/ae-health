package feo.health.catalog_service.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DoctorsServiceDto {
    private String title;
    private BigDecimal price;
}
