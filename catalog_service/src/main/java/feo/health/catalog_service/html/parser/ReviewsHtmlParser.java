package feo.health.catalog_service.html.parser;

import feo.health.catalog_service.model.dto.ReviewDto;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewsHtmlParser {

    public List<ReviewDto> parseDoctorReviews(Document document) {
        List<ReviewDto> reviews = new ArrayList<>();

        Elements reviewCards = document.select("div.b-review-card[itemprop=review]");

        for (Element card : reviewCards) {
            ReviewDto dto = new ReviewDto();

            Element textElem = card.selectFirst(".b-review-card__comment-wrapper .b-review-card__comment");
            dto.setText(textElem != null ? textElem.text().trim() : null);

            Element dateElem = card.selectFirst("div[itemprop=\"datePublished\"]");
            if (dateElem != null) {
                String rawDate = dateElem.attr("content");
                dto.setDate(Date.valueOf(rawDate));
            }

            Element ratingElem = card.selectFirst(".review-card-tooltips__stars span.ui-text_subtitle-2");
            if (ratingElem != null) {
                try {
                    float rating = Float.parseFloat(ratingElem.text().replace(',', '.'));
                    dto.setRating(rating);
                } catch (Exception ignored) {
                }
            }

            reviews.add(dto);
        }

        return reviews;
    }
}
