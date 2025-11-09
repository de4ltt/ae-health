package feo.health.catalog_service.controller;

import feo.health.catalog_service.model.dto.ClinicDto;
import feo.health.catalog_service.model.dto.ServiceDto;
import feo.health.catalog_service.service.clinic.ClinicService;
import feo.health.catalog_service.service.services.ServicesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/catalog/services")
@AllArgsConstructor
public class ServiceController {

    private final ServicesService servicesService;
    private final ClinicService clinicService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<ServiceDto>>> searchServices(@RequestParam String q) {
        return servicesService.searchServices(q)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{uri}/clinics")
    public CompletableFuture<ResponseEntity<List<ClinicDto>>> getClinicsByService(@PathVariable String uri) {
        return clinicService.getClinicsByService(uri)
                .thenApply(ResponseEntity::ok);
    }
}