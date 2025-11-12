package feo.health.catalog_service.service;

import feo.health.catalog_service.html.client.DrugHtmlClient;
import feo.health.catalog_service.html.parser.DrugHtmlParser;
import feo.health.catalog_service.model.dto.DrugDto;
import feo.health.catalog_service.service.drug.DrugServiceImpl;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DrugServiceImplTest {

    @Mock
    private DrugHtmlParser parser;

    @Mock
    private DrugHtmlClient client;

    @InjectMocks
    private DrugServiceImpl drugService;

    @Test
    void searchDrugs_success() throws IOException, ExecutionException, InterruptedException {
        String query = "aspirin";
        Document doc = mock(Document.class);
        List<DrugDto> drugs = Collections.singletonList(new DrugDto());

        when(client.getDrugsPage(query)).thenReturn(doc);
        when(parser.parseDrugs(doc)).thenReturn(drugs);

        CompletableFuture<List<DrugDto>> future = drugService.searchDrugs(query);
        List<DrugDto> result = future.get();

        assertEquals(1, result.size());
    }

    @Test
    void getDrugInfo_success() throws IOException, ExecutionException, InterruptedException {
        String uri = "/drug";
        Document doc = mock(Document.class);
        DrugDto dto = new DrugDto();

        when(client.getDrugPage(uri)).thenReturn(doc);
        when(parser.parseDrug(doc)).thenReturn(dto);

        CompletableFuture<DrugDto> future = drugService.getDrugInfo(uri);
        DrugDto result = future.get();

        assertEquals(dto, result);
        assertEquals(uri, result.getLink());
    }
}