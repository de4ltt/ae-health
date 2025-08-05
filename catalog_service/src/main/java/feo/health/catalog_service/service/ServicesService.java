package feo.health.catalog_service.service;

import feo.health.catalog_service.dto.ServiceDto;

import java.util.List;

public interface ServicesService {
    List<ServiceDto> searchServices(String query);
}
