package feo.health.catalog_service.html.parser;

import feo.health.catalog_service.model.dto.ClinicDto;
import feo.health.catalog_service.model.dto.ReviewDto;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
public class ClinicHtmlParser {

    public ClinicDto parseClinic(Document document, Document reviewsDocument) {
        ClinicDto clinicDto = new ClinicDto();

        Element nameElem = document.selectFirst("[data-qa=lpu_card_heading_lpu_name]");
        clinicDto.setName(nameElem != null ? nameElem.text().trim() : null);

        Element addressElem = document.selectFirst("[data-qa=lpu_card_btn_addr_text]");
        clinicDto.setAddress(addressElem != null ? addressElem.text().trim() : null);

        Element phoneElem = document.selectFirst("[data-qa=lpu_card_btn_phone_text]");
        clinicDto.setPhoneNumber(phoneElem != null ? phoneElem.text().trim() : null);

        Element imageElem = document.selectFirst("img[data-qa=lpu_card_logo_image]");
        clinicDto.setImageUri(imageElem != null ? "https://prodoctorov.ru" + imageElem.attr("src") : null);

        List<ReviewDto> reviews = new ArrayList<>();
        for (Element reviewElement : reviewsDocument.select("div.b-review-card[itemprop=review]")) {
            ReviewDto reviewDto = new ReviewDto();

            Element textElement = reviewElement.selectFirst(".b-review-card__comment-wrapper .b-review-card__comment");
            reviewDto.setText(textElement != null ? textElement.text().trim() : null);

            Element dateElement = reviewElement.selectFirst("[itemprop=datePublished]");
            reviewDto.setDate(dateElement != null ? Date.valueOf(dateElement.attr("content")) : null);

            Element ratingMeta = reviewElement.selectFirst("meta[itemprop=ratingValue]");
            if (ratingMeta != null) {
                float ratingFloat = Float.parseFloat(ratingMeta.attr("content"));
                reviewDto.setRating(ratingFloat / 20);
            }

            reviews.add(reviewDto);
        }
        clinicDto.setReviews(reviews);

        return clinicDto;
    }

    public List<ClinicDto> parseClinics(Document document) {

        final Function<Element, ClinicDto> extractSingleClinic = el -> {

            ClinicDto clinicDto = new ClinicDto();

            Element nameLink = el.selectFirst("a[data-qa=lpu_card_heading]");
            if (null != nameLink) {
                clinicDto.setLink(ClinicDto.clearClinicLink(nameLink.attr("href")));
                clinicDto.setName(nameLink.text().trim());
            }

            Element image = el.selectFirst("img[data-qa=lpu_card_logo_image]");
            clinicDto.setImageUri(image != null ? "https://prodoctorov.ru" + image.attr("src") : null);

            Element address = el.selectFirst("span[data-qa=lpu_card_btn_addr_text]");
            clinicDto.setAddress(address != null ? address.text().trim() : null);

            Element phone = el.selectFirst("span[data-qa=lpu_card_btn_phone_text]");
            clinicDto.setPhoneNumber(phone != null ? phone.text().trim() : null);

            clinicDto.setItemType("clinic");

            return clinicDto;
        };

        return document.select("div[data-qa^=lpu-id-]")
                .stream().map(extractSingleClinic).toList();
    }

    public List<ClinicDto> parseClinicTypes(Document document) {

        final Function<Element, ClinicDto> extractSingleClinicType = el -> {

            ClinicDto clinicDto = new ClinicDto();

            clinicDto.setLink(ClinicDto.clearClinicLink(el.attr("href")));

            Element title = el.selectFirst("span.b-list-icon-link__text");
            clinicDto.setName(title != null ? title.text().trim() : "");

            clinicDto.setItemType("clinic-type");

            return clinicDto;
        };

        return document.select("a.b-list-icon-link.b-section-box__elem")
                .stream().map(extractSingleClinicType).toList();
    }

    public List<ClinicDto> parseClinicsAndClinicTypes(Document document) {
        return document.select("section:has(h2:matches((?i)Клиники|Типы клиник))")
                .select("div.b-section-box__elem, a.b-list-icon-link.b-section-box__elem")
                .stream()
                .map(el -> {
                    if (el.tagName().equals("div"))
                        return parseClinicCard(el);
                    else
                        return parseClinicTypeLink(el);
                })
                .toList();
    }

    public List<ClinicDto> parseDoctorClinics(Document document) {
        return document.select("div.doctor-page-list-lpu.pa-6").stream().map(clinic -> {

            Element nameElem = clinic.selectFirst("a.text-subtitle-1.primary--text.text-decoration-none");
            String link = nameElem != null ? ClinicDto.clearClinicLink(nameElem.attr("href")) : null;
            String name = nameElem != null ? nameElem.text() : null;

            Element addressElem = clinic.selectFirst("div.d-flex.align-center.text-body-1.primary--text.py-2.cursor-pointer.mt-4");
            String address = addressElem != null ? addressElem.text() : null;

            Element phoneElem = clinic.selectFirst("div.d-flex.align-center.text-decoration-none.text-body-1.py-2.mt-2");
            String phone = phoneElem != null ? phoneElem.text() : null;

            ClinicDto clinicDto = new ClinicDto();
            clinicDto.setName(name);
            clinicDto.setLink(link);
            clinicDto.setAddress(address);
            clinicDto.setPhoneNumber(phone);

            return clinicDto;
        }).toList();
    }


    private ClinicDto parseClinicCard(Element el) {
        ClinicDto dto = new ClinicDto();

        Element nameEl = el.selectFirst("a[data-qa=lpu_card_heading]");

        if (nameEl != null) {
            dto.setName(nameEl.text().trim());
            dto.setLink(ClinicDto.clearClinicLink(nameEl.attr("href")));
        }

        Element img = el.selectFirst("img[data-qa=lpu_card_logo_image]");
        dto.setImageUri(img != null ? "https://prodoctorov.ru" + img.attr("src") : null);

        Element address = el.selectFirst("div[data-qa=lpu_card_btn_addr_text]");
        dto.setAddress(address != null ? address.text().trim() : null);

        Element phone = el.selectFirst("div[data-qa=lpu_card_btn_phone_text]");
        dto.setPhoneNumber(phone != null ? phone.text().trim() : null);

        dto.setItemType("clinic");

        return dto;
    }

    private ClinicDto parseClinicTypeLink(Element el) {
        ClinicDto dto = new ClinicDto();

        Element name = el.selectFirst("span.b-list-icon-link__text");

        dto.setName(name != null ? name.text().trim() : null);
        dto.setLink(ClinicDto.clearClinicLink(el.attr("href")));
        dto.setItemType("clinic-type");

        return dto;
    }
}
