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
public class DiseaseGuessResponse {

    Map<String, Float> possibleDiseases;
    List<String> doctors;
    String generalResponse;

    public static DiseaseGuessResponse from(JsonNode jsonNode) {

        Map<String, Float> diseases = new HashMap<>();
        if (jsonNode.has("possibleDiseases"))
            for (JsonNode diseaseNode : jsonNode.get("possibleDiseases")) {
                String name = diseaseNode.get("name").asText();
                float probability = (float) diseaseNode.get("probability").asDouble();
                diseases.put(name, probability);
            }

        List<String> doctors = new ArrayList<>();
        if (jsonNode.has("doctors"))
            for (JsonNode doctorNode : jsonNode.get("doctors"))
                doctors.add(doctorNode.asText());

        String generalResponse = jsonNode.has("generalResponse") ? jsonNode.get("generalResponse").asText() : null;

        return DiseaseGuessResponse.builder()
                .possibleDiseases(diseases)
                .doctors(doctors)
                .generalResponse(generalResponse)
                .build();
    }
}