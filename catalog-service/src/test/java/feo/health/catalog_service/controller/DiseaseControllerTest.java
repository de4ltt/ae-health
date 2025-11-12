package feo.health.catalog_service.controller;

import feo.health.catalog_service.model.dto.DiseaseDto;
import feo.health.catalog_service.service.disease.DiseaseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiseaseControllerTest {

    @Mock
    private DiseaseService diseaseService;

    @InjectMocks
    private DiseaseController diseaseController;

    @Test
    void searchDiseases_success() throws ExecutionException, InterruptedException {
        String q = "flu";
        List<DiseaseDto> diseases = Collections.singletonList(new DiseaseDto());

        when(diseaseService.searchDiseases(q)).thenReturn(CompletableFuture.completedFuture(diseases));

        CompletableFuture<ResponseEntity<List<DiseaseDto>>> future = diseaseController.searchDiseases(q);
        ResponseEntity<List<DiseaseDto>> response = future.get();

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(diseases, response.getBody());
    }

    @Test
    void getDiseaseInfo_success() throws ExecutionException, InterruptedException {
        String uri = "/disease";
        String article = "Article content";

        when(diseaseService.getDiseaseArticle(uri)).thenReturn(CompletableFuture.completedFuture(article));

        CompletableFuture<ResponseEntity<String>> future = diseaseController.getDiseaseInfo(uri);
        ResponseEntity<String> response = future.get();

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(MediaType.TEXT_HTML, response.getHeaders().getContentType());
        assertEquals(article, response.getBody());
    }
}