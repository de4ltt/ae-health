package feo.health.ai_service.model.request;

import lombok.Value;

import java.util.List;

@Value
public class DiseaseGuessRequest {

    List<String> symptoms;

    @Override
    public String toString() {
        return "симптомы: [ " + String.join(", ", symptoms) + "]";
    }
}
