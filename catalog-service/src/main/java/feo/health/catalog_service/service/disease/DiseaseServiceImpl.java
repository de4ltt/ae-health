package feo.health.catalog_service.service.disease;

import feo.health.catalog_service.html.client.DiseaseHtmlClient;
import feo.health.catalog_service.html.parser.DiseaseHtmlParser;
import feo.health.catalog_service.model.dto.DiseaseDto;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class DiseaseServiceImpl implements DiseaseService {

    private final DiseaseHtmlClient client;
    private final DiseaseHtmlParser parser;

    @Override
    public String getDiseaseArticle(String uri) {
        try {
            Document articleDocument = client.getDiseaseArticlePage(uri);
            return parser.parseDiseaseArticle(articleDocument);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DiseaseDto> searchDiseases(String query) {
        try {
            Document diseasesDocument = client.getDiseasesPage(query);
            return parser.parseDiseasesPage(diseasesDocument);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
