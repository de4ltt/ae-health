package feo.health.catalog_service.html.parser;

import feo.health.catalog_service.dto.DoctorDto;
import feo.health.catalog_service.dto.SpecialityDto;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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

    public List<DoctorDto> parseClinicDoctors(Document document) {

        List<DoctorDto> doctors = new ArrayList<>();
        for (Element doctorElem : document.select(".b-doctor-card")) {
            DoctorDto doctorDto = new DoctorDto();

            Element nameElement = doctorElem.selectFirst(".b-doctor-card__name-surname");
            doctorDto.setName(nameElement != null ? nameElement.text().trim() : null);

            Element uriElement = doctorElem.selectFirst("a.b-doctor-card__name-link");
            doctorDto.setUri(uriElement != null ? "https://prodoctorov.ru" + uriElement.attr("href") : null);

            String experienceStr = doctorElem.attr("data-experience");
            doctorDto.setExperience(!experienceStr.isEmpty() ? Byte.parseByte(experienceStr) : null);

            Element img = doctorElem.selectFirst(".b-profile-card__img-wrap img");
            doctorDto.setImageUri(img != null ? "https://prodoctorov.ru" + img.attr("src") : null);

            Element ratingElem = doctorElem.selectFirst("div.b-stars-rate__progress");
            doctorDto.setRating(ratingElem != null ? extractDoctorRating(ratingElem) : null);

            doctorDto.setItemType("doctor");

            List<SpecialityDto> specialities = new ArrayList<>();
            Element specElement = doctorElem.selectFirst(".b-doctor-card__spec");
            if (specElement != null) {
                List<String> specs = Arrays.stream(specElement.text().split(",\\s*")).map(String::toLowerCase).toList();
                for (String s : specs) {
                    SpecialityDto spec = new SpecialityDto();
                    spec.setName(s.trim());
                    specialities.add(spec);
                }
            }

            doctorDto.setSpecialities(specialities);

            doctors.add(doctorDto);
        }

        return doctors;
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

            Element nameLink = el.selectFirst("a.b-card__name-link, a.b-doctor-card__name-link.text-wrap");
            doctorDto.setUri(nameLink != null ? "https://prodoctorov.ru" + nameLink.attr("href") : null);
            doctorDto.setName(nameLink != null ? nameLink.text().trim() : null);

            Element img = el.selectFirst("img.b-card__avatar-img, img[class=b-profile-card__img]");
            doctorDto.setImageUri(img != null ? "https://prodoctorov.ru" + img.attr("src") : null);

            Element speciality = el.selectFirst("p.b-card__category, div.b-doctor-card__spec");
            if (speciality != null && speciality.hasText()) {
                List<SpecialityDto> specialityDtos = Stream.of(speciality.text().split(", "))
                        .map(name -> {
                            SpecialityDto specialityDto = new SpecialityDto();
                            specialityDto.setName(name.toLowerCase());
                            specialityDto.setUri(null);
                            return specialityDto;
                        }).toList();
                doctorDto.setSpecialities(specialityDtos);
            }

            Element experienceElem = el.selectFirst("div.ui-text.ui-text_subtitle-1");
            if (experienceElem != null) {
                Matcher matcher = Pattern.compile("(\\d+)\\s+лет").matcher(experienceElem.text());
                if (matcher.find())
                    doctorDto.setExperience(Byte.parseByte(matcher.group(1)));
            }
            Element ratingElem = el.selectFirst("div.b-stars-rate__progress");
            doctorDto.setRating(ratingElem != null ? extractDoctorRating(ratingElem) : null);

            doctorDto.setItemType("doctor");

            return doctorDto;
        };

        return document.select("article.b-card.b-card_list-item.b-section-box__elem, div.b-doctor-card")
                .stream().map(extractSingleDoctors).toList();
    }

    public List<DoctorDto> parseDoctorSpecialities(Document document) {

        final Function<Element, DoctorDto> extractSingleSpecialities = element -> {

            DoctorDto doctorDto = new DoctorDto();

            String uri = "https://prodoctorov.ru" + element.attr("href");
            doctorDto.setUri(uri);

            Element speciality = element.selectFirst("span.b-list-icon-link__text.ui-text.ui-text_body-1");
            if (speciality != null && speciality.hasText()) {
                List<SpecialityDto> specialityDtos = Stream.of(speciality.text().split(", "))
                        .map(name -> {
                            SpecialityDto specialityDto = new SpecialityDto();
                            specialityDto.setName(name.toLowerCase());
                            specialityDto.setUri(null);
                            return specialityDto;
                        }).toList();
                doctorDto.setSpecialities(specialityDtos);
            }

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

        Element speciality = element.selectFirst("p.b-card__category.ui-text.ui-text_body-1.ui-kit-color-text-secondary");
        if (speciality != null && speciality.hasText()) {
            List<SpecialityDto> specialityDtos = Stream.of(speciality.text().split(", "))
                    .map(name -> {
                        SpecialityDto specialityDto = new SpecialityDto();
                        specialityDto.setName(name.toLowerCase());
                        specialityDto.setUri(null);
                        return specialityDto;
                    }).toList();
            dto.setSpecialities(specialityDtos);
        }

        dto.setItemType("doctor");

        return dto;
    }

    private DoctorDto parseSpecialityLink(Element element) {

        DoctorDto doctorDto = new DoctorDto();

        doctorDto.setUri("https://prodoctorov.ru" + element.attr("href"));

        Element speciality = element.selectFirst("span.b-list-icon-link__text.ui-text.ui-text_body-1");

        doctorDto.setName(speciality != null ? speciality.text().toLowerCase() : null);
        doctorDto.setItemType("speciality");

        return doctorDto;
    }

    private Float extractDoctorRating(Element ratingElement) {

        String style = ratingElement.attr("style"); // "width: 6.4400em"
        Pattern pattern = Pattern.compile("width:\\s*([0-9.]+)em");
        Matcher matcher = pattern.matcher(style);

        if (matcher.find())
            return Float.parseFloat(matcher.group(1)) / 6.44f * 5;

        return null;
    }
}
