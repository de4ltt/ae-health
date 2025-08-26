package feo.health.catalog_service.html.parser;

import feo.health.catalog_service.model.dto.ClinicDto;
import feo.health.catalog_service.model.dto.DoctorDto;
import feo.health.catalog_service.model.dto.SpecialityDto;
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
                .map(el ->
                        el.tagName().equals("article") ?
                                parseDoctorCard(el) : parseSpecialityLink(el))
                .toList();
    }

    public List<DoctorDto> parseClinicDoctors(Document document) {

        List<DoctorDto> doctors = new ArrayList<>();
        for (Element doctorElem : document.select(".b-doctor-card")) {
            DoctorDto doctorDto = new DoctorDto();

            Element nameElement = doctorElem.selectFirst(".b-doctor-card__name-surname");
            doctorDto.setName(nameElement != null ? nameElement.text().trim() : null);

            Element uriElement = doctorElem.selectFirst("a.b-doctor-card__name-link");
            doctorDto.setLink(uriElement != null ? DoctorDto.clearDoctorLink(uriElement.attr("href")) : null);

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

        Element nameElem = document.selectFirst(
                "span.d-block.ui-text.ui-text_h5.ui-kit-color-text.mb-2, " +
                        "span.d-block.text-h5.text--text.mb-2");
        doctorDto.setName(nameElem != null ? nameElem.text() : null);

        Element imageElem = document.selectFirst("div.b-doctor-intro__left-side.mr-8 img[itemprop=image]");
        doctorDto.setImageUri(imageElem != null ? "https://prodoctorov.ru" + imageElem.attr("src") : null);

        Elements specElems = document.select(".b-doctor-intro__spec");
        doctorDto.setSpecialities(specElems.stream().map(el -> {
            SpecialityDto specialityDto = new SpecialityDto();
            specialityDto.setName(el.text().toLowerCase());
            specialityDto.setLink(SpecialityDto.clearSpecialityLink(el.attr("href")));
            return specialityDto;
        }).toList());

        Element expElem = document.selectFirst("div.b-doctor-intro__documents-plate .text-subtitle-1");
        if (expElem != null) {
            Matcher matcher = Pattern.compile("(\\d+)\\s+лет").matcher(expElem.text());
            if (matcher.find())
                doctorDto.setExperience(Byte.parseByte(matcher.group(1)));
        }

        Element ratingElem = document.selectFirst("div.b-stars-rate__progress");
        if (ratingElem != null) {
            String style = ratingElem.attr("style");
            Matcher matcher = Pattern.compile("([0-9.]+)em").matcher(style);
            if (matcher.find()) {
                doctorDto.setRating(Float.parseFloat(matcher.group(1)) / 6.44f * 5f);
            }
        }

        Elements clinicElems = document.select("div.doctor-page-lpu-list");
        List<ClinicDto> clinics = new ArrayList<>();
        for (Element el : clinicElems) {
            ClinicDto clinic = new ClinicDto();
            clinic.setName(el.text());
            clinic.setLink(el.attr("href"));

            Element addrElem = el.parent().selectFirst("[data-lpu-addr-id]");
            if (addrElem != null) {
                clinic.setAddress(addrElem.text());
            }

            clinics.add(clinic);
        }

        doctorDto.setItemType("doctor");

        return doctorDto;
    }


    public List<DoctorDto> parseDoctors(Document document) {

        final Function<Element, DoctorDto> extractSingleDoctors = el -> {

            DoctorDto doctorDto = new DoctorDto();

            Element nameLink = el.selectFirst("a.b-card__name-link, a.b-doctor-card__name-link.text-wrap");
            if (nameLink != null) {
                doctorDto.setLink(DoctorDto.clearDoctorLink(nameLink.attr("href")));
                doctorDto.setName(nameLink.text().trim());
            }

            Element img = el.selectFirst("img.b-card__avatar-img, img[class=b-profile-card__img]");
            doctorDto.setImageUri(img != null ? "https://prodoctorov.ru" + img.attr("src") : null);

            Element speciality = el.selectFirst("p.b-card__category, div.b-doctor-card__spec");
            if (speciality != null && speciality.hasText()) {
                List<SpecialityDto> specialityDtos = Stream.of(speciality.text().split(", "))
                        .map(name -> {
                            SpecialityDto specialityDto = new SpecialityDto();
                            specialityDto.setName(name.toLowerCase());
                            specialityDto.setLink(null);
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

            Element titleElem = element.selectFirst("span.b-list-icon-link__text.text-body-1");
            String title = titleElem != null ? titleElem.text() : null;
            doctorDto.setName(title);

            String uri = DoctorDto.clearDoctorLink(element.attr("href"));
            doctorDto.setLink(uri);

//            Element speciality = element.selectFirst("span.b-list-icon-link__text.ui-text.ui-text_body-1");
//            doctorDto.setName(doctorDto.getName().isEmpty() && speciality != null ? speciality.text() : null);

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
            dto.setLink(DoctorDto.clearDoctorLink(nameLink.attr("href")));
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
                        specialityDto.setLink(null);
                        return specialityDto;
                    }).toList();
            dto.setSpecialities(specialityDtos);
        }

        dto.setItemType("doctor");

        return dto;
    }

    private DoctorDto parseSpecialityLink(Element element) {

        DoctorDto doctorDto = new DoctorDto();

        doctorDto.setLink(DoctorDto.clearDoctorLink(element.attr("href")));

        Element speciality = element.selectFirst("span.b-list-icon-link__text.ui-text.ui-text_body-1");

        doctorDto.setName(speciality != null ? speciality.text().toLowerCase() : null);
        doctorDto.setItemType("speciality");

        return doctorDto;
    }

    private Float extractDoctorRating(Element ratingElement) {

        String style = ratingElement.attr("style");
        Pattern pattern = Pattern.compile("width:\\s*([0-9.]+)em");
        Matcher matcher = pattern.matcher(style);

        return matcher.find() ? Float.parseFloat(matcher.group(1)) / 6.44f * 5 : null;
    }
}
