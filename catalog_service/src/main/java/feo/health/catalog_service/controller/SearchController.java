package feo.health.catalog_service.controller;

import feo.health.catalog_service.dto.*;
import feo.health.catalog_service.entity.Doctor;
import feo.health.catalog_service.service.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog/search")
@AllArgsConstructor
public class SearchController {

    private final GeneralSearchService generalSearchService;
    private final DoctorService doctorService;
    private final ClinicService clinicService;
    private final ServicesService servicesService;
    private final PharmacyService pharmacyService;

    @GetMapping
    ResponseEntity<SearchDto> search(@RequestParam String q, @RequestParam(defaultValue = "true") Boolean located) {
        SearchDto result = generalSearchService.search(q, located);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/doctors")
    ResponseEntity<List<DoctorDto>> searchDoctors(@RequestParam String q) {
        List<DoctorDto> result = doctorService.searchDoctors(q);
        return ResponseEntity.ok(result);
    }

    @GetMapping("doctors/speciality")
    ResponseEntity<List<DoctorDto>> searchDoctorsBySpeciality(@RequestParam String uri) {
        List<DoctorDto> result = doctorService.getDoctorsBySpeciality(uri);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/clinics")
    ResponseEntity<List<ClinicDto>> searchClinics(
            @RequestParam String q, @RequestParam(defaultValue = "true") Boolean located
    ) {
        List<ClinicDto> result = clinicService.searchClinics(q, located);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/services")
    ResponseEntity<List<ServiceDto>> searchServices(@RequestParam String q) {
        List<ServiceDto> result = servicesService.searchServices(q);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/pharmacies")
    ResponseEntity<List<PharmacyDto>> searchPharmacies(
            @RequestParam Double lat,
            @RequestParam Double lon,
            @RequestParam(defaultValue = "500") Integer radius
    ) {
        List<PharmacyDto> result = pharmacyService.searchPharmacies(radius, lat, lon);
        return ResponseEntity.ok(result);
    }

}
