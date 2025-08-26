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

    public Document getClinicsByTypePage(String latinLink) throws IOException {
        String uri = String.format("https://prodoctorov.ru/krasnodar/top/%s/", latinLink);
        return Jsoup.connect(uri).get();
    }

    public Document getClinicPage(String latinLink) throws IOException {
        String uri = String.format("https://prodoctorov.ru/krasnodar/lpu/%s/", latinLink);
        return Jsoup.connect(uri).get();
    }

    public Document getClinicDoctorsPage(String latinLink) throws IOException {
        String uri = String.format("https://prodoctorov.ru/krasnodar/lpu/%s/vrachi", latinLink);
        return Jsoup.connect(uri).get();
    }

    public Document getClinicsByServicesPage(String latinLink) throws IOException {
        String uri = String.format("https://prodoctorov.ru/krasnodar/%s/", latinLink.replace("_", "/"));
        return Jsoup.connect(uri).get();
    }

    public Document getClinicReviewsPage(String latinLink) throws IOException {
        String uri = String.format("https://prodoctorov.ru/krasnodar/lpu/%s/otzivi", latinLink);
        return Jsoup.connect(uri).get();
    }
}
