package feo.health.catalog_service.controller;

import feo.health.catalog_service.dto.DoctorDto;
import feo.health.catalog_service.service.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/catalog/doctors")
@AllArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    ResponseEntity<?> getDoctorInfo(@RequestParam String uri) {
        DoctorDto doctor = doctorService.getDoctorInfo(uri);
        return ResponseEntity.ok(doctor);
    }

}
