package feo.health.catalog_service.controller;

import feo.health.catalog_service.dto.ClinicDto;
import feo.health.catalog_service.service.ClinicService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/catalog/clinics")
@AllArgsConstructor
public class ClinicController {

    private final ClinicService clinicService;

    @GetMapping
    ResponseEntity<ClinicDto> getDoctorInfo(
            @RequestParam String uri,
            @RequestParam(defaultValue = "true") Boolean located
    ) {
        ClinicDto clinic = clinicService.getClinicInfo(uri, located);
        return ResponseEntity.ok(clinic);
    }

}
