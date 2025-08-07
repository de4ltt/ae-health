package feo.health.catalog_service.html.parser;

import feo.health.catalog_service.dto.DoctorsServiceDto;
import feo.health.catalog_service.dto.ServiceDto;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceHtmlParser {

    public List<DoctorsServiceDto> parseDoctorServices(Document document) {
        List<DoctorsServiceDto> services = new ArrayList<>();

        Elements serviceElements = document.select("div[data-qa=doctor_service_prices_price_item]");

        serviceElements.forEach(el -> {
            String title = el.selectFirst("div[data-qa=doctor_service_prices_price_item_name]").text();
            String priceText = el.selectFirst("div[data-qa=doctor_service_prices_price_item_price]").text();

            BigDecimal price = null;
            price = new BigDecimal(priceText.replaceAll("\\D", ""));

            DoctorsServiceDto dto = new DoctorsServiceDto();
            dto.setTitle(title);
            dto.setPrice(price);

            services.add(dto);
        });

        return services;
    }

    public List<ServiceDto> parseServices(Document document) {
        return document.select("section:has(h2:contains(Услуги))")
                .select("a.b-list-icon-link.b-section-box__elem")
                .stream()
                .map(el -> {
                    ServiceDto dto = new ServiceDto();
                    dto.setUri("https://prodoctorov.ru" + el.attr("href"));
                    Element name = el.selectFirst("span.b-list-icon-link__text");
                    dto.setName(name != null ? name.text().trim() : null);
                    return dto;
                })
                .toList();
    }
}