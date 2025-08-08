package feo.health.catalog_service.html.client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DrugHtmlClient {

    public Document getDrugsPage(String query) throws IOException {
        String uri = String.format("https://protabletky.ru/krasnodar/find/?q=%s", query);
        return Jsoup.connect(uri).get();
    }

    public Document getDrugPage(String nameUri) throws IOException {
        String uri = String.format("https://protabletky.ru/%s", nameUri);
        return Jsoup.connect(uri).get();
    }

}
