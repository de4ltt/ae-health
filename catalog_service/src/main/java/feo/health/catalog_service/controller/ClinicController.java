package feo.health.catalog_service.controller;

import feo.health.catalog_service.dto.ClinicDto;
import feo.health.catalog_service.dto.DoctorDto;
import feo.health.catalog_service.service.ClinicService;
import feo.health.catalog_service.service.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog/clinic")
@AllArgsConstructor
public class ClinicController {

    private final ClinicService clinicService;
    private final DoctorService doctorService;

    @GetMapping
    ResponseEntity<ClinicDto> getClinicInfo(
            @RequestParam String uri,
            @RequestParam(defaultValue = "true") Boolean located
    ) {
        ClinicDto clinic = clinicService.getClinicInfo(uri, located);
        return ResponseEntity.ok(clinic);
    }

    @GetMapping("/doctors")
    ResponseEntity<List<DoctorDto>> getClinicDoctors(@RequestParam String uri) {
        List<DoctorDto> clinicDtos = doctorService.getClinicDoctors(uri);
        return ResponseEntity.ok(clinicDtos);
    }

}
