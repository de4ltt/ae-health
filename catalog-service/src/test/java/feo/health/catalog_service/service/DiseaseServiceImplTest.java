package feo.health.catalog_service.service;

import feo.health.catalog_service.html.client.DiseaseHtmlClient;
import feo.health.catalog_service.html.parser.DiseaseHtmlParser;
import feo.health.catalog_service.model.dto.DiseaseDto;
import feo.health.catalog_service.service.disease.DiseaseServiceImpl;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiseaseServiceImplTest {

    @Mock
    private DiseaseHtmlClient client;

    @Mock
    private DiseaseHtmlParser parser;

    @InjectMocks
    private DiseaseServiceImpl diseaseService;

    @Test
    void getDiseaseArticle_success() throws IOException, ExecutionException, InterruptedException {
        String uri = "/disease";
        Document doc = mock(Document.class);
        String article = "Article text";

        when(client.getDiseaseArticlePage(uri)).thenReturn(doc);
        when(parser.parseDiseaseArticle(doc)).thenReturn(article);

        CompletableFuture<String> future = diseaseService.getDiseaseArticle(uri);
        String result = future.get();

        assertEquals(article, result);
    }

    @Test
    void searchDiseases_success() throws IOException, ExecutionException, InterruptedException {
        String query = "flu";
        Document doc = mock(Document.class);
        List<DiseaseDto> diseases = Collections.singletonList(new DiseaseDto());

        when(client.getDiseasesPage(query)).thenReturn(doc);
        when(parser.parseDiseasesPage(doc)).thenReturn(diseases);

        CompletableFuture<List<DiseaseDto>> future = diseaseService.searchDiseases(query);
        List<DiseaseDto> result = future.get();

        assertEquals(1, result.size());
    }
}