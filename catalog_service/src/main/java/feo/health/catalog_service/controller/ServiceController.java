package feo.health.catalog_service.controller;

import feo.health.catalog_service.dto.ClinicDto;
import feo.health.catalog_service.entity.Clinic;
import feo.health.catalog_service.service.ClinicService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog/service")
@AllArgsConstructor
public class ServiceController {

    private ClinicService clinicService;

    @GetMapping
    public ResponseEntity<List<ClinicDto>> getClinicsByService(@RequestParam String uri) {
        List<ClinicDto> result = clinicService.getClinicsByService(uri);
        return ResponseEntity.ok(result);
    }

}
