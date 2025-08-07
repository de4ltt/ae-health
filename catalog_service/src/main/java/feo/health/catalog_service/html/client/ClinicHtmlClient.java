package feo.health.catalog_service.html.client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ClinicHtmlClient {

    public Document getClinicsPage(String query) throws IOException {
        String uri = String.format("https://prodoctorov.ru/krasnodar/find/?q=%s&filter=lpus", query);
        return Jsoup.connect(uri).get();
    }

    public Document getClinicTypesPage(String query) throws IOException {
        String uri = String.format("https://prodoctorov.ru/krasnodar/find/?q=%s&filter=lputypes", query);
        return Jsoup.connect(uri).get();
    }

    public Document getClinicPage(String uri) throws IOException {
        return Jsoup.connect(uri).get();
    }

    public Document getClinicReviewsPage(String clinicUri) throws IOException {
        String uri = clinicUri + "otzivi";
        return Jsoup.connect(uri).get();
    }

}
