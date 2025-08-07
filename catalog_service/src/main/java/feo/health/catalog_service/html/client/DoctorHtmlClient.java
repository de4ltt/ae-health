package feo.health.catalog_service.html.client;

import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
@AllArgsConstructor
public class DoctorHtmlClient {

    private final WebDriver webDriver;

    public Document getDoctorPage(String doctorUri) throws IOException {
        try {
            webDriver.get(doctorUri);

            new WebDriverWait(webDriver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector("div.doctor-page-list-lpu")));

            String pageSource = webDriver.getPageSource();

            if (pageSource == null)
                throw new IOException("Couldn't retrieve " + doctorUri);

            return Jsoup.parse(pageSource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Document getDoctorsPage(String query) throws IOException {
        final String uri = String.format("https://prodoctorov.ru/krasnodar/find/?q=%s&filter=doctors", query);
        return Jsoup.connect(uri).get();
    }

    public Document getDoctorSpecialitiesPage(String query) throws IOException {
        final String uri = String.format("https://prodoctorov.ru/krasnodar/find/?q=%s&filter=specialities", query);
        return Jsoup.connect(uri).get();
    }

    public Document getDoctorsBySpecialityPage(String uri) throws IOException {
        return Jsoup.connect(uri).get();
    }
}
