package feo.health.catalog_service.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ServiceDto {

    final String itemType = "service";

    @NotNull
    String name;

    @NotNull
    String link;

    public static String clearServiceLink(String link) {
        String result = link
                .replace("https://prodoctorov.ru/", "")
                .replace("krasnodar/", "")
                .replaceFirst("uslugi", "")
                .replaceFirst("/", "");
        if (result.indexOf("/") == 0) result = result.substring(1, result.length() - 1);
        if (result.lastIndexOf("/") == result.length() - 1) result = result.substring(0, result.length() - 1);
        return result.replaceAll("/", "_");
    }
}
