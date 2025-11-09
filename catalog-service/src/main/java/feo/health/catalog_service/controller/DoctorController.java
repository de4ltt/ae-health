package feo.health.catalog_service.controller;

import feo.health.catalog_service.model.dto.ClinicDto;
import feo.health.catalog_service.model.dto.DoctorDto;
import feo.health.catalog_service.service.doctor.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/catalog/doctors")
@AllArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    public CompletableFuture<ResponseEntity<List<DoctorDto>>> searchDoctors(@RequestParam(required = false) String q) {
        return doctorService.searchDoctors(q)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/speciality/{uri}")
    public CompletableFuture<ResponseEntity<List<DoctorDto>>> getBySpeciality(@PathVariable String uri) {
        return doctorService.getDoctorsBySpeciality(uri)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{uri}")
    public CompletableFuture<ResponseEntity<DoctorDto>> getDoctor(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable String uri
    ) {
        return doctorService.getDoctorInfo(uri, userId)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{uri}/clinics")
    public CompletableFuture<ResponseEntity<List<ClinicDto>>> getDoctorClinics(@PathVariable String uri) {
        return doctorService.getDoctorClinics(uri)
                .thenApply(ResponseEntity::ok);
    }
}