package feo.health.ai_service.model.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Value
@Builder
public class SuggestionResponse {

    List<String> doctors;
    List<String> drugs;
    Map<String, Float> possibleDiseases;
    String generalAnswer;

    public static SuggestionResponse from(JsonNode jsonNode) {

        List<String> doctors = new ArrayList<>();
        if (jsonNode.has("doctors"))
            for (JsonNode node : jsonNode.get("doctors"))
                doctors.add(node.asText());

        List<String> drugs = new ArrayList<>();
        if (jsonNode.has("drugs"))
            for (JsonNode node : jsonNode.get("drugs"))
                drugs.add(node.asText());

        Map<String, Float> possibleDiseases = new HashMap<>();
        if (jsonNode.has("possibleDiseases"))
            for (JsonNode diseaseNode : jsonNode.get("possibleDiseases")) {
                String name = diseaseNode.get("name").asText();
                float probability = (float) diseaseNode.get("probability").asDouble();
                possibleDiseases.put(name, probability);
            }

        String generalAnswer = jsonNode.has("generalAnswer") ? jsonNode.get("generalAnswer").asText() : null;

        return SuggestionResponse.builder()
                .doctors(doctors)
                .drugs(drugs)
                .possibleDiseases(possibleDiseases)
                .generalAnswer(generalAnswer)
                .build();
    }
}
