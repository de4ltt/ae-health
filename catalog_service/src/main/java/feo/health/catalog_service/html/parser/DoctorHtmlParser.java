package feo.health.catalog_service.html.parser;

import feo.health.catalog_service.dto.DoctorDto;
import feo.health.catalog_service.dto.SpecialityDto;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DoctorHtmlParser {

    public List<DoctorDto> parseDoctorsAndSpecialities(Document document) {
        return document.select("section:has(h2:contains(Специальности)), section:has(h2:contains(Врачи))")
                .select("a.b-list-icon-link.b-section-box__elem, article.b-card")
                .stream()
                .map(el -> {
                    if (el.tagName().equals("article")) {
                        return parseDoctorCard(el);
                    } else {
                        return parseSpecialityLink(el);
                    }
                })
                .toList();
    }

    public DoctorDto parseDoctor(Document document) {

        DoctorDto doctorDto = new DoctorDto();

        Element nameElem = document.selectFirst("span.d-block.ui-text.ui-text_h5.ui-kit-color-text.mb-2");
        doctorDto.setName(nameElem != null ? nameElem.text() : null);

        Element imageElem = document.selectFirst("div.b-doctor-intro__left-side.mr-8").selectFirst("img[itemprop=image]");
        doctorDto.setImageUri(imageElem != null ? "https://prodoctorov.ru" + imageElem.attr("src") : null);

        Elements specElems = document.select(".b-doctor-intro__spec");
        doctorDto.setSpecialities(specElems.stream().map(el -> {
            SpecialityDto specialityDto = new SpecialityDto();
            specialityDto.setName(el.text().toLowerCase());
            specialityDto.setUri("https://prodoctorov.ru" + el.attr("href"));

            return specialityDto;
        }).toList());

        Element expElem = document.selectFirst(".b-doctor-intro__documents-plate .ui-text_subtitle-1");
        if (expElem != null) {
            Matcher matcher = Pattern.compile("(\\d+)\\s+лет").matcher(expElem.text());
            if (matcher.find())
                doctorDto.setExperience(Byte.parseByte(matcher.group(1)));
        }

        return doctorDto;
    }

    public List<DoctorDto> parseDoctors(Document document) {

        final Function<Element, DoctorDto> extractSingleDoctors = el -> {

            DoctorDto doctorDto = new DoctorDto();

            Element nameLink = el.selectFirst("a.b-card__name-link");
            doctorDto.setUri(nameLink != null ? "https://prodoctorov.ru" + nameLink.attr("href") : null);
            doctorDto.setName(nameLink != null ? nameLink.text().trim() : null);

            Element img = el.selectFirst("img.b-card__avatar-img");
            doctorDto.setImageUri(img != null ? "https://prodoctorov.ru" + img.attr("src") : null);

            Element speciality = el.selectFirst("p.b-card__category");
            SpecialityDto specialityDto = new SpecialityDto();
            specialityDto.setName(speciality.text().toLowerCase());
            specialityDto.setUri(speciality.attr("href"));
            doctorDto.setSpecialities(List.of(specialityDto));

            doctorDto.setItemType("doctor");

            return doctorDto;
        };

        return document.select("article.b-card.b-card_list-item.b-section-box__elem")
                .stream().map(extractSingleDoctors).toList();
    }

    public List<DoctorDto> parseDoctorSpecialities(Document document) {

        final Function<Element, DoctorDto> extractSingleSpecialities = element -> {

            DoctorDto doctorDto = new DoctorDto();

            String uri = "https://prodoctorov.ru" + element.attr("href");
            doctorDto.setUri(uri);

            Element speciality = element.selectFirst("span.b-list-icon-link__text.ui-text.ui-text_body-1");
            SpecialityDto specialityDto = new SpecialityDto();
            specialityDto.setName(speciality.text().toLowerCase());
            specialityDto.setUri(speciality.attr("href"));
            doctorDto.setSpecialities(List.of(specialityDto));

            doctorDto.setItemType("speciality");

            return doctorDto;
        };

        return document.select("a.b-list-icon-link.b-section-box__elem")
                .stream().map(extractSingleSpecialities).toList();
    }

    private DoctorDto parseDoctorCard(Element element) {

        DoctorDto dto = new DoctorDto();

        Element nameLink = element.selectFirst("a.b-card__name-link");
        if (nameLink != null) {
            dto.setUri("https://prodoctorov.ru" + nameLink.attr("href"));
            dto.setName(nameLink.text().trim());
        }

        Element img = element.selectFirst("img.b-card__avatar-img");
        dto.setImageUri(img != null ? "https://prodoctorov.ru" + img.attr("src") : null);

        Element speciality = element.selectFirst("p.b-card__category");
        SpecialityDto specialityDto = new SpecialityDto();
        specialityDto.setName(speciality.text());
        specialityDto.setName(speciality.attr("href"));
        dto.setSpecialities(List.of(specialityDto));
        dto.setItemType("doctor");

        return dto;
    }

    private DoctorDto parseSpecialityLink(Element element) {

        DoctorDto doctorDto = new DoctorDto();

        doctorDto.setUri("https://prodoctorov.ru" + element.attr("href"));

        Element speciality = element.selectFirst("span.b-list-icon-link__text");
        SpecialityDto specialityDto = new SpecialityDto();
        specialityDto.setName(speciality.text());
        specialityDto.setName(speciality.attr("href"));
        doctorDto.setSpecialities(List.of(specialityDto));

        doctorDto.setItemType("speciality");

        return doctorDto;
    }

}
