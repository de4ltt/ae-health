package feo.health.catalog_service.controller;

import feo.health.catalog_service.dto.DoctorDto;
import feo.health.catalog_service.service.doctor.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog/doctors")
@AllArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    public ResponseEntity<List<DoctorDto>> searchDoctors(@RequestParam(required = false) String q) {
        return ResponseEntity.ok(doctorService.searchDoctors(q));
    }

    @GetMapping("/speciality/{uri}")
    public ResponseEntity<List<DoctorDto>> getBySpeciality(@PathVariable String uri) {
        return ResponseEntity.ok(doctorService.getDoctorsBySpeciality(uri));
    }

    @GetMapping("/{uri}")
    public ResponseEntity<DoctorDto> getDoctor(@PathVariable String uri) {
        return ResponseEntity.ok(doctorService.getDoctorInfo(uri));
    }
}