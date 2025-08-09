package feo.health.catalog_service.html.parser;

import feo.health.catalog_service.model.dto.DrugDto;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DrugHtmlParser {

    public List<DrugDto> parseDrugs(Document document) {
        List<DrugDto> drugs = new ArrayList<>();

        for (Element row : document.select("#tab-best table.lpu-list tbody tr")) {
            Element nameLink = row.selectFirst("td:nth-child(3) a[href]");
            if (nameLink == null) continue;

            DrugDto dto = new DrugDto();
            dto.setName(nameLink.text().trim());

            String href = nameLink.attr("href");
            dto.setLink(DrugDto.clearDrugLink(href));

            Element img = row.selectFirst("td:nth-child(2) img");
            if (img != null) {
                String src = img.attr("src");
                dto.setImageUri(src.startsWith("http") ? src : "https://protabletky.ru" + src);
            }

            drugs.add(dto);
        }

        return drugs;
    }


    public DrugDto parseDrug(Document document) {

        DrugDto drugDto = new DrugDto();

        drugDto.setName(textOrEmpty(document.selectFirst("h1[itemprop=name]")));
        drugDto.setLatinName(textOrEmpty(document.selectFirst(".drug_name_latin")));

        drugDto.setEffectiveness(starsToRating(document.select(".drug_right__stars_tooltipster tr:contains(Эффективность) .stars span")));
        drugDto.setPriceQuality(starsToRating(document.select(".drug_right__stars_tooltipster tr:contains(Цена/качество) .stars span")));
        drugDto.setSideEffects(starsToRating(document.select(".drug_right__stars_tooltipster tr:contains(Побочные эффекты) .stars span")));

        Element reviewsElem = document.selectFirst(".reviews_count");
        drugDto.setReviewsCount(reviewsElem != null ? Integer.parseInt(reviewsElem.text().replaceAll("\\D+", "")) : null);

        Element imageUriElem = document.selectFirst("a[class=drug_image]");
        drugDto.setImageUri(imageUriElem != null ? "https://protabletky.ru" + imageUriElem.attr("href") : null);

        List<DrugDto.DrugFormDto> forms = new ArrayList<>(List.of());
        for (Element row : document.select(".drug_forms tbody tr")) {
            DrugDto.DrugFormDto form = new DrugDto.DrugFormDto();
            Element formImg = row.selectFirst(".drug_form_img img");
            if (formImg != null) {
                form.setFormName(formImg.attr("title").isEmpty() ? formImg.attr("alt") : formImg.attr("title"));
            } else continue;
            form.setDosage(textOrEmpty(row.selectFirst(".drug_dozirovka")));
            form.setPackaging(textOrEmpty(row.selectFirst(".drug_kolvo")));

            Element storageImg = row.selectFirst("td:nth-child(4) img");
            if (storageImg != null) form.setStorage(storageImg.attr("alt"));

            Element saleImg = row.selectFirst("td:nth-child(5) img");
            if (saleImg != null) form.setSale(saleImg.attr("alt"));

            form.setShelfLife(textOrEmpty(row.selectFirst(".pharm_godnost")));
            forms.add(form);
        }
        drugDto.setForms(forms);

        List<DrugDto.InstructionSectionDto> sections = new ArrayList<>();
        for (Element section : document.select(".instruction_section")) {
            DrugDto.InstructionSectionDto secDto = new DrugDto.InstructionSectionDto();

            Element titleEl = section.selectFirst("h4, h3, h2");
            secDto.setTitle(titleEl != null ? titleEl.text().trim() : "");

            StringBuilder textBuilder = new StringBuilder();
            for (Element el : section.select(".instruction_text, p, li, span")) {
                String part = el.text().trim();
                if (!part.isEmpty()) {
                    textBuilder.append(part).append("\n");
                }
            }

            for (Element img : section.select("img")) {
                if (!img.attr("alt").isEmpty()) {
                    textBuilder.append(img.attr("alt")).append("\n");
                }
                if (!img.attr("title").isEmpty()) {
                    textBuilder.append(img.attr("title")).append("\n");
                }
            }

            secDto.setText(textBuilder.toString().trim());
            if (!secDto.getText().isEmpty()) {
                sections.add(secDto);
            }
        }
        drugDto.setInstructionSections(sections);

        drugDto.setRating(DrugDto.calculateRating(drugDto));

        return drugDto;
    }

    private String textOrEmpty(Element el) {
        return el == null ? "" : el.text().trim();
    }

    private Double starsToRating(Elements starSpans) {
        if (starSpans.isEmpty()) return null;
        String style = starSpans.first().attr("style");
        String width = style.replaceAll("[^0-9]", "");
        return width.isEmpty() ? null : (Double.parseDouble(width) / 16);
    }
}
