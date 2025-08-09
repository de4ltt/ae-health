package feo.health.catalog_service.model.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OverpassPharmaciesResponse {

    private List<Element> elements;

    @Data
    public static class Element {
        private double lat;
        private double lon;
        private Map<String, String> tags;
    }
}
