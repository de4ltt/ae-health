package feo.health.catalog_service.model.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class HistoryDto {
    Long userId;
    String itemType;
    Long itemId;
}
