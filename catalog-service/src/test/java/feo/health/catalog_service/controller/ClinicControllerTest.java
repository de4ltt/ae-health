package feo.health.catalog_service.controller;

import feo.health.catalog_service.model.dto.ClinicDto;
import feo.health.catalog_service.model.dto.DoctorDto;
import feo.health.catalog_service.service.clinic.ClinicService;
import feo.health.catalog_service.service.doctor.DoctorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClinicControllerTest {

    @Mock
    private ClinicService clinicService;

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private ClinicController clinicController;

    @Test
    void searchClinics_success() throws ExecutionException, InterruptedException {
        String q = "test";
        Boolean located = true;
        List<ClinicDto> clinics = Collections.singletonList(ClinicDto.builder().build());

        when(clinicService.searchClinics(q, located)).thenReturn(CompletableFuture.completedFuture(clinics));

        CompletableFuture<ResponseEntity<List<ClinicDto>>> future = clinicController.searchClinics(q, located);
        ResponseEntity<List<ClinicDto>> response = future.get();

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(clinics, response.getBody());
    }

    @Test
    void getClinicsByType_success() throws ExecutionException, InterruptedException {
        String uri = "/type";
        List<ClinicDto> clinics = Collections.singletonList(ClinicDto.builder().build());

        when(clinicService.getClinicsByType(uri)).thenReturn(CompletableFuture.completedFuture(clinics));

        CompletableFuture<ResponseEntity<List<ClinicDto>>> future = clinicController.getClinicsByType(uri);
        ResponseEntity<List<ClinicDto>> response = future.get();

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(clinics, response.getBody());
    }

    @Test
    void getClinic_success() throws ExecutionException, InterruptedException {
        Long userId = 1L;
        String uri = "/clinic";
        Boolean located = true;
        ClinicDto dto = ClinicDto.builder().build();

        when(clinicService.getClinicInfo(uri, located, userId)).thenReturn(CompletableFuture.completedFuture(dto));

        CompletableFuture<ResponseEntity<ClinicDto>> future = clinicController.getClinic(userId, uri, located);
        ResponseEntity<ClinicDto> response = future.get();

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(dto, response.getBody());
    }

    @Test
    void getClinicDoctors_success() throws ExecutionException, InterruptedException {
        String uri = "/clinic";
        List<DoctorDto> doctors = Collections.singletonList(new DoctorDto());

        when(doctorService.getClinicDoctors(uri)).thenReturn(CompletableFuture.completedFuture(doctors));

        CompletableFuture<ResponseEntity<List<DoctorDto>>> future = clinicController.getClinicDoctors(uri);
        ResponseEntity<List<DoctorDto>> response = future.get();

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(doctors, response.getBody());
    }
}