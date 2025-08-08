package feo.health.catalog_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ServiceDto {

    @NotNull
    final String itemType = "service";

    @NotNull
    String name;

    @NotNull
    String uri;

}
