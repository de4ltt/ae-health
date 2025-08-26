package feo.health.ai_service.model.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
@Builder
public class ProcedureDescriptionResponse {

    String name;
    String description;
    List<String> contradictions;
    List<String> indications;

    public static ProcedureDescriptionResponse from(JsonNode jsonNode) {

        String name = jsonNode.has("name") ? jsonNode.get("name").asText() : null;
        String description = jsonNode.has("description") ? jsonNode.get("description").asText() : null;

        List<String> contradictions = new ArrayList<>();
        if (jsonNode.has("contradictions"))
            for (JsonNode node : jsonNode.get("contradictions"))
                contradictions.add(node.asText());

        List<String> indications = new ArrayList<>();
        if (jsonNode.has("indications"))
            for (JsonNode node : jsonNode.get("indications"))
                indications.add(node.asText());

        return ProcedureDescriptionResponse.builder()
                .name(name)
                .description(description)
                .contradictions(contradictions)
                .indications(indications)
                .build();
    }
}
