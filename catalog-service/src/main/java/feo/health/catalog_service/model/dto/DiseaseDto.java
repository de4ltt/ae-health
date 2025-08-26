package feo.health.catalog_service.model.dto;

import lombok.Data;

@Data
public class DiseaseDto {

    private String name;
    private String link;

    public static String clearDiseaseLink(String link) {
        return link
                .replace("https://probolezny.ru/", "")
                .replaceAll("/", "");
    }
}
