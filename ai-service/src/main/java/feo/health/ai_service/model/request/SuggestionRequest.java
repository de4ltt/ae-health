package feo.health.ai_service.model.request;

import lombok.Value;

@Value
public class SuggestionRequest {

    String input;

    @Override
    public String toString() {
        return input;
    }
}
