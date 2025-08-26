package feo.health.catalog_service.html.parser;

import feo.health.catalog_service.model.dto.DiseaseDto;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Component
public class DiseaseHtmlParser {

    public List<DiseaseDto> parseDiseasesPage(Document document) {
        Element h1 = document.selectFirst("h1#clinical_cases");

        List<DiseaseDto> diseaseDtos = new ArrayList<>();
        if (h1 != null) {
            Element prev = h1.previousElementSibling();
            while (prev != null) {
                if (prev.tagName().equals("div") && prev.hasClass("b-articles-list__item")) {

                    Element nameLinkElem = prev.selectFirst("a");

                    if (nameLinkElem != null) {
                        DiseaseDto diseaseDto = new DiseaseDto();
                        diseaseDto.setName(nameLinkElem.text());
                        diseaseDto.setLink(DiseaseDto.clearDiseaseLink(nameLinkElem.attr("href")));

                        diseaseDtos.add(diseaseDto);
                    }
                }
                prev = prev.previousElementSibling();
            }
            Collections.reverse(diseaseDtos);
        }

        return diseaseDtos;
    }

    public String parseDiseaseArticle(Document document) {

        Element articleBody = document.selectFirst("div[itemprop=articleBody]");

        if (articleBody == null)
            return "";

        Elements links = articleBody.select("a");
        for (Element link : links) {
            link.replaceWith(new Element("span").text(link.text()));
        }

        articleBody.select("sup").remove();

        Elements images = articleBody.select("img");
        for (Element image : images) {
            image.replaceWith(
                    new Element("img")
                            .attr(
                                    "src",
                                    "https://probolezny.ru" + image.attr("src")
                            )
            );
        }

        articleBody.selectFirst("div.b-article__date-wrapper").replaceWith(new Element("div"));

        return articleBody.html();
    }
}
