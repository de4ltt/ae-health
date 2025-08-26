package feo.health.catalog_service.html.client;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.PlaywrightException;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class DoctorHtmlClient {

    private final Browser browser;

    public Document getDoctorClinicsPage(String latinLink) {

        String uri = String.format("https://prodoctorov.ru/krasnodar/vrach/%s", latinLink);

        try (Page page = browser.newPage()) {
            page.navigate(uri);
            page.waitForSelector("#doctor-page-lpu-list");
            String content = page.content();
            return Jsoup.parse(content);
        } catch (PlaywrightException e) {
            throw new RuntimeException("Failed to load page " + uri, e);
        }
    }

    public Document getDoctorPage(String latinLink) throws IOException {
        String uri = String.format("https://prodoctorov.ru/krasnodar/vrach/%s", latinLink);
        return Jsoup.connect(uri).get();
    }

    public Document getDoctorsPage(String query) throws IOException {
        final String uri = String.format("https://prodoctorov.ru/krasnodar/find/?q=%s&filter=doctors", query);
        return Jsoup.connect(uri).get();
    }

    public Document getDoctorSpecialitiesPage(String query) throws IOException {
        final String uri = String.format("https://prodoctorov.ru/krasnodar/find/?q=%s&filter=specialities", query);
        return Jsoup.connect(uri).get();
    }

    public Document getDoctorsBySpecialityPage(String latinLink) throws IOException {
        String uri = String.format("https://prodoctorov.ru/krasnodar/%s", latinLink);
        return Jsoup.connect(uri).get();
    }
}
