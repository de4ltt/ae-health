package feo.health.catalog_service.controller;

import feo.health.catalog_service.model.dto.DrugDto;
import feo.health.catalog_service.service.drug.DrugService;
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
class DrugsControllerTest {

    @Mock
    private DrugService drugService;

    @InjectMocks
    private DrugsController drugsController;

    @Test
    void searchDrugs_success() throws ExecutionException, InterruptedException {
        String q = "aspirin";
        List<DrugDto> drugs = Collections.singletonList(new DrugDto());

        when(drugService.searchDrugs(q)).thenReturn(CompletableFuture.completedFuture(drugs));

        CompletableFuture<ResponseEntity<List<DrugDto>>> future = drugsController.searchDrugs(q);
        ResponseEntity<List<DrugDto>> response = future.get();

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(drugs, response.getBody());
    }

    @Test
    void getDrugInfo_success() throws ExecutionException, InterruptedException {
        String uri = "/drug";
        DrugDto dto = new DrugDto();

        when(drugService.getDrugInfo(uri)).thenReturn(CompletableFuture.completedFuture(dto));

        CompletableFuture<ResponseEntity<DrugDto>> future = drugsController.getDrugInfo(uri);
        ResponseEntity<DrugDto> response = future.get();

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(dto, response.getBody());
    }
}