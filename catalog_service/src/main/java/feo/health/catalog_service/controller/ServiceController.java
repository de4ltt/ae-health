package feo.health.catalog_service.controller;

import feo.health.catalog_service.dto.ClinicDto;
import feo.health.catalog_service.dto.ServiceDto;
import feo.health.catalog_service.service.ClinicService;
import feo.health.catalog_service.service.ServicesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog/services")
@AllArgsConstructor
public class ServiceController {

    private final ServicesService servicesService;
    private final ClinicService clinicService;

    @GetMapping
    public ResponseEntity<List<ServiceDto>> searchServices(@RequestParam String q) {
        return ResponseEntity.ok(servicesService.searchServices(q));
    }

    @GetMapping("/{uri}/clinics")
    public ResponseEntity<List<ClinicDto>> getClinicsByService(@PathVariable String uri) {
        return ResponseEntity.ok(clinicService.getClinicsByService(uri));
    }
}