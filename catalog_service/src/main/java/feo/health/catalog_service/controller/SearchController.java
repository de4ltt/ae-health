package feo.health.catalog_service.controller;

import feo.health.catalog_service.dto.ClinicDto;
import feo.health.catalog_service.dto.DoctorDto;
import feo.health.catalog_service.dto.SearchDto;
import feo.health.catalog_service.dto.ServiceDto;
import feo.health.catalog_service.service.ClinicService;
import feo.health.catalog_service.service.DoctorService;
import feo.health.catalog_service.service.GeneralSearchService;
import feo.health.catalog_service.service.ServicesService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog/search")
@AllArgsConstructor
public class SearchController {

    private final GeneralSearchService generalSearchService;
    private final DoctorService doctorService;
    private final ClinicService clinicService;
    private final ServicesService servicesService;

    @GetMapping
    ResponseEntity<?> search(@RequestParam String q, @RequestParam(defaultValue = "true") Boolean located) {
        SearchDto result = generalSearchService.search(q, located);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/doctors")
    ResponseEntity<?> searchDoctors(@RequestParam String q) {
        List<DoctorDto> result = doctorService.searchDoctors(q);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/clinics")
    ResponseEntity<?> searchClinics(@RequestParam String q, @RequestParam(defaultValue = "true") Boolean located) {
        List<ClinicDto> result = clinicService.searchClinics(q, located);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/services")
    ResponseEntity<?> searchServices(@RequestParam String q) {
        List<ServiceDto> result = servicesService.searchServices(q);
        return ResponseEntity.ok(result);
    }
}
