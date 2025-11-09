package feo.health.catalog_service.service.disease;

import feo.health.catalog_service.html.client.DiseaseHtmlClient;
import feo.health.catalog_service.html.parser.DiseaseHtmlParser;
import feo.health.catalog_service.model.dto.DiseaseDto;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class DiseaseServiceImpl implements DiseaseService {

    private final DiseaseHtmlClient client;
    private final DiseaseHtmlParser parser;

    @Override
    @Async
    public CompletableFuture<String> getDiseaseArticle(String uri) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Document articleDocument = client.getDiseaseArticlePage(uri);
                return parser.parseDiseaseArticle(articleDocument);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    @Async
    public CompletableFuture<List<DiseaseDto>> searchDiseases(String query) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Document diseasesDocument = client.getDiseasesPage(query);
                return parser.parseDiseasesPage(diseasesDocument);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}