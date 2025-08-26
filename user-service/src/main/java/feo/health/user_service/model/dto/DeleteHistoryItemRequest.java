package feo.health.user_service.model.dto;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class DeleteHistoryItemRequest {
    CatalogItemDto catalogItem;
    LocalDateTime dateTime;
}
