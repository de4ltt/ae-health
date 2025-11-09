package feo.health.catalog_service.service.services;

import feo.health.catalog_service.model.dto.ServiceDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ServicesService {
    CompletableFuture<List<ServiceDto>> searchServices(String query);
}