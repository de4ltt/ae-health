package feo.health.catalog_service.controller;

import feo.health.catalog_service.dto.ClinicDto;
import feo.health.catalog_service.dto.DoctorDto;
import feo.health.catalog_service.service.clinic.ClinicService;
import feo.health.catalog_service.service.doctor.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog/clinics")
@AllArgsConstructor
public class ClinicController {

    private final ClinicService clinicService;
    private final DoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<ClinicDto>> searchClinics(
            @RequestParam String q,
            @RequestParam(defaultValue = "true") Boolean located
    ) {
        return ResponseEntity.ok(clinicService.searchClinics(q, located));
    }

    @GetMapping("/{uri}")
    public ResponseEntity<ClinicDto> getClinic(
            @PathVariable String uri,
            @RequestParam(defaultValue = "true") Boolean located
    ) {
        return ResponseEntity.ok(clinicService.getClinicInfo(uri, located));
    }

    @GetMapping("/{uri}/doctors")
    public ResponseEntity<List<DoctorDto>> getClinicDoctors(@PathVariable String uri) {
        return ResponseEntity.ok(doctorService.getClinicDoctors(uri));
    }
}

