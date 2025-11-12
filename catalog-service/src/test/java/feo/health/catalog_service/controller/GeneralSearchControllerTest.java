package feo.health.catalog_service.controller;

import feo.health.catalog_service.model.dto.SearchDto;
import feo.health.catalog_service.service.general_search.GeneralSearchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeneralSearchControllerTest {

    @Mock
    private GeneralSearchService generalSearchService;

    @InjectMocks
    private GeneralSearchController generalSearchController;

    @Test
    void search_success() throws ExecutionException, InterruptedException {
        String q = "test";
        Boolean located = true;
        SearchDto dto = new SearchDto();

        when(generalSearchService.search(q, located)).thenReturn(CompletableFuture.completedFuture(dto));

        CompletableFuture<ResponseEntity<SearchDto>> future = generalSearchController.search(q, located);
        ResponseEntity<SearchDto> response = future.get();

        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(dto, response.getBody());
    }
}