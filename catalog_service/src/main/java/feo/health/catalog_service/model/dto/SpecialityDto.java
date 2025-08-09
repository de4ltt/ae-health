package feo.health.catalog_service.model.dto;

import lombok.Data;

@Data
public class SpecialityDto {

    private String name;

    private String link;

    public static String clearSpecialityLink(String link) {
        return link
                .replace("https://prodoctorov.ru/", "")
                .replace("krasnodar", "")
                .replaceAll("/", "");
    }
}
