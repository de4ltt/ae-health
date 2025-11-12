package feo.health.catalog_service.controller;

import feo.health.catalog_service.model.dto.ClinicDto;
import feo.health.catalog_service.model.dto.ServiceDto;
import feo.health.catalog_service.service.clinic.ClinicService;
import feo.health.catalog_service.service.services.ServicesService;
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
class ServiceControllerTest {

    @Mock
    private ServicesService servicesService;

    @Mock
    private ClinicService clinicService;

    @InjectMocks
    private ServiceController serviceController;

    @Test
    void searchServices_success() throws ExecutionException, InterruptedException {
        String q = "test";
        List<ServiceDto> services = Collections.singletonList(new ServiceDto());

        when(servicesService.searchServices(q)).thenReturn(CompletableFuture.completedFuture(services));

        CompletableFuture<ResponseEntity<List<ServiceDto>>> future = serviceController.searchServices(q);
        ResponseEntity<List<ServiceDto>> response = future.get();

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(services, response.getBody());
    }

    @Test
    void getClinicsByService_success() throws ExecutionException, InterruptedException {
        String uri = "/service";
        List<ClinicDto> clinics = Collections.singletonList(ClinicDto.builder().build());

        when(clinicService.getClinicsByService(uri)).thenReturn(CompletableFuture.completedFuture(clinics));

        CompletableFuture<ResponseEntity<List<ClinicDto>>> future = serviceController.getClinicsByService(uri);
        ResponseEntity<List<ClinicDto>> response = future.get();

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(clinics, response.getBody());
    }
}