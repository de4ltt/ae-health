package feo.health.user_service.mapper;

import catalog.Catalog;
import feo.health.user_service.model.dto.CatalogItemDto;
import org.springframework.stereotype.Component;

@Component
public class CatalogItemMapper {

    public CatalogItemDto toDto(Catalog.CatalogItem catalogItem) {
        return CatalogItemDto.builder()
                .name(catalogItem.getName())
                .link(catalogItem.getLink())
                .imageUrl(catalogItem.getImageUri())
                .type(catalogItem.getType())
                .build();
    }
}
