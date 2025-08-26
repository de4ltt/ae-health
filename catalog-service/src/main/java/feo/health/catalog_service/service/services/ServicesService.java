package feo.health.catalog_service.service.services;

import feo.health.catalog_service.model.dto.ServiceDto;

import java.util.List;

public interface ServicesService {
    List<ServiceDto> searchServices(String query);
}
