package feo.health.user_service.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CatalogItemDto {
    private String name;
    private String type;
    private String link;
    private String imageUrl;
    private LocalDateTime dateTime;
}