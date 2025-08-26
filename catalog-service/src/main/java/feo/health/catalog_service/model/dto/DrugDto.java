package feo.health.catalog_service.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class DrugDto {

    private String name;
    private String latinName;
    private String link;
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

    public static String clearDrugLink(String link) {
        return link
                .replaceAll("//protabletky.ru/", "")
                .replaceAll("/", "");
    }

    public static Double calculateRating(DrugDto drugDto) {

        int count = 0;
        double score = 0d;

        if (drugDto.getEffectiveness() != null) {
            count++;
            score += drugDto.getEffectiveness();
        }

        if (drugDto.getPriceQuality() != null) {
            count++;
            score += drugDto.getPriceQuality();
        }

        if (drugDto.getSideEffects() != null) {
            count++;
            score += drugDto.getSideEffects();
        }

        return count > 0 ? score / count : null;
    }

}



