package feo.health.catalog_service.html.client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ReviewsHtmlClient {

    public Document getDoctorReviewsPage(String doctorUri) throws IOException {
        final String reviewsUrl = String.format("https://prodoctorov.ru/krasnodar/vrach/%s/otzivi", doctorUri);
        return Jsoup.connect(reviewsUrl).get();
    }
}
