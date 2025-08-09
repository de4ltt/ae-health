package feo.health.catalog_service.html.client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DiseaseHtmlClient {

    public Document getDiseaseArticlePage(String nameUri) throws IOException {
        String uri = String.format("https://probolezny.ru/%s/", nameUri);
        return Jsoup.connect(uri).get();
    }

    public Document getDiseasesPage(String query) throws IOException {
        String uri = String.format("https://probolezny.ru/?q=%s", query);
        return Jsoup.connect(uri).get();
    }
}
