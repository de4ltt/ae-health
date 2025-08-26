package feo.health.catalog_service.service.drug;

import feo.health.catalog_service.html.client.DrugHtmlClient;
import feo.health.catalog_service.html.parser.DrugHtmlParser;
import feo.health.catalog_service.model.dto.DrugDto;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class DrugServiceImpl implements DrugService {

    final DrugHtmlParser parser;
    final DrugHtmlClient client;

    @Override
    public List<DrugDto> searchDrugs(String query) {
        try {
            Document drugsDocument = client.getDrugsPage(query);
            return parser.parseDrugs(drugsDocument);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DrugDto getDrugInfo(String drugUri) {
        try {
            Document drugDocument = client.getDrugPage(drugUri);
            DrugDto result = parser.parseDrug(drugDocument);
            result.setLink(drugUri);
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
