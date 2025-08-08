package feo.health.catalog_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class DrugDto {

    private String name;
    private String latinName;

    private String uri;
    private String imageUri;

    private Double effectiveness;

    private Double rating;
    private Double priceQuality;
    private Double sideEffects;
    private Integer reviewsCount;

    private List<DrugFormDto> forms;
    private List<InstructionSectionDto> instructionSections;

    @Data
    public static class DrugFormDto {
        private String formName;
        private String dosage;
        private String packaging;
        private String storage;
        private String sale;
        private String shelfLife;
    }

    @Data
    public static class InstructionSectionDto {
        private String title;
        private String text;
    }

}



