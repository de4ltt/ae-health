package feo.health.catalog_service.controller;

import feo.health.catalog_service.model.dto.PharmacyDto;
import feo.health.catalog_service.service.pharmacy.PharmacyService;
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
class PharmacyControllerTest {

    @Mock
    private PharmacyService pharmacyService;

    @InjectMocks
    private PharmacyController pharmacyController;

    @Test
    void searchPharmacies_success() throws ExecutionException, InterruptedException {
        Double lat = 0.0;
        Double lon = 0.0;
        Integer radius = 500;
        List<PharmacyDto> pharmacies = Collections.singletonList(PharmacyDto.builder().build());

        when(pharmacyService.searchPharmacies(radius, lat, lon)).thenReturn(CompletableFuture.completedFuture(pharmacies));

        CompletableFuture<ResponseEntity<List<PharmacyDto>>> future = pharmacyController.searchPharmacies(lat, lon, radius);
        ResponseEntity<List<PharmacyDto>> response = future.get();

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(pharmacies, response.getBody());
    }

    @Test
    void visitPharmacy_success() throws ExecutionException, InterruptedException {
        Long userId = 1L;
        PharmacyDto dto = PharmacyDto.builder().build();

        when(pharmacyService.visitPharmacy(dto, userId)).thenReturn(CompletableFuture.completedFuture(null));

        CompletableFuture<ResponseEntity<Void>> future = pharmacyController.visitPharmacy(userId, dto);
        ResponseEntity<Void> response = future.get();

        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    void getPharmacyById_success() throws ExecutionException, InterruptedException {
        Long id = 1L;
        PharmacyDto dto = PharmacyDto.builder().build();

        when(pharmacyService.getPharmacyById(id)).thenReturn(CompletableFuture.completedFuture(dto));

        CompletableFuture<ResponseEntity<PharmacyDto>> future = pharmacyController.getPharmacyById(id);
        ResponseEntity<PharmacyDto> response = future.get();

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(dto, response.getBody());
    }
}