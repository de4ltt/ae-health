package feo.health.catalog_service.controller;

import feo.health.catalog_service.model.dto.ClinicDto;
import feo.health.catalog_service.model.dto.DoctorDto;
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
class DoctorControllerTest {

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;

    @Test
    void searchDoctors_success() throws ExecutionException, InterruptedException {
        String q = "test";
        List<DoctorDto> doctors = Collections.singletonList(new DoctorDto());

        when(doctorService.searchDoctors(q)).thenReturn(CompletableFuture.completedFuture(doctors));

        CompletableFuture<ResponseEntity<List<DoctorDto>>> future = doctorController.searchDoctors(q);
        ResponseEntity<List<DoctorDto>> response = future.get();

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(doctors, response.getBody());
    }

    @Test
    void getBySpeciality_success() throws ExecutionException, InterruptedException {
        String uri = "/speciality";
        List<DoctorDto> doctors = Collections.singletonList(new DoctorDto());

        when(doctorService.getDoctorsBySpeciality(uri)).thenReturn(CompletableFuture.completedFuture(doctors));

        CompletableFuture<ResponseEntity<List<DoctorDto>>> future = doctorController.getBySpeciality(uri);
        ResponseEntity<List<DoctorDto>> response = future.get();

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(doctors, response.getBody());
    }

    @Test
    void getDoctor_success() throws ExecutionException, InterruptedException {
        Long userId = 1L;
        String uri = "/doctor";
        DoctorDto dto = new DoctorDto();

        when(doctorService.getDoctorInfo(uri, userId)).thenReturn(CompletableFuture.completedFuture(dto));

        CompletableFuture<ResponseEntity<DoctorDto>> future = doctorController.getDoctor(userId, uri);
        ResponseEntity<DoctorDto> response = future.get();

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(dto, response.getBody());
    }

    @Test
    void getDoctorClinics_success() throws ExecutionException, InterruptedException {
        String uri = "/doctor";
        List<ClinicDto> clinics = Collections.singletonList(ClinicDto.builder().build());

        when(doctorService.getDoctorClinics(uri)).thenReturn(CompletableFuture.completedFuture(clinics));

        CompletableFuture<ResponseEntity<List<ClinicDto>>> future = doctorController.getDoctorClinics(uri);
        ResponseEntity<List<ClinicDto>> response = future.get();

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(clinics, response.getBody());
    }
}