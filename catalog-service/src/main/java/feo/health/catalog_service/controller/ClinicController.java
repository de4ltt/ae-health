package feo.health.catalog_service.controller;

import feo.health.catalog_service.model.dto.ClinicDto;
import feo.health.catalog_service.model.dto.DoctorDto;
import feo.health.catalog_service.service.clinic.ClinicService;
import feo.health.catalog_service.service.doctor.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/catalog/clinics")
@AllArgsConstructor
public class ClinicController {

    private final ClinicService clinicService;
    private final DoctorService doctorService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<ClinicDto>>> searchClinics(
            @RequestParam String q,
            @RequestParam(defaultValue = "true") Boolean located
    ) {
        return clinicService.searchClinics(q, located)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{uri}/clinics")
    public CompletableFuture<ResponseEntity<List<ClinicDto>>> getClinicsByType(@PathVariable String uri) {
        return clinicService.getClinicsByType(uri)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{uri}")
    public CompletableFuture<ResponseEntity<ClinicDto>> getClinic(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable String uri,
            @RequestParam(defaultValue = "true") Boolean located
    ) {
        return clinicService.getClinicInfo(uri, located, userId)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{uri}/doctors")
    public CompletableFuture<ResponseEntity<List<DoctorDto>>> getClinicDoctors(@PathVariable String uri) {
        return doctorService.getClinicDoctors(uri)
                .thenApply(ResponseEntity::ok);
    }
}