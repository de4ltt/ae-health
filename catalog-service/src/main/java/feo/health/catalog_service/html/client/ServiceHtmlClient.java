package feo.health.catalog_service.html.client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ServiceHtmlClient {

    public Document getServicesPage(String query) throws IOException {
        final String url = String.format("https://prodoctorov.ru/krasnodar/find/?q=%s&filter=services", query);
        return Jsoup.connect(url).get();
    }
}
