package feo.health.catalog_service.service.drug;

import feo.health.catalog_service.html.client.DrugHtmlClient;
import feo.health.catalog_service.html.parser.DrugHtmlParser;
import feo.health.catalog_service.model.dto.DrugDto;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class DrugServiceImpl implements DrugService {

    final DrugHtmlParser parser;
    final DrugHtmlClient client;

    @Override
    @Async
    public CompletableFuture<List<DrugDto>> searchDrugs(String query) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Document drugsDocument = client.getDrugsPage(query);
                return parser.parseDrugs(drugsDocument);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    @Async
    public CompletableFuture<DrugDto> getDrugInfo(String drugUri) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Document drugDocument = client.getDrugPage(drugUri);
                DrugDto result = parser.parseDrug(drugDocument);
                result.setLink(drugUri);
                return result;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}