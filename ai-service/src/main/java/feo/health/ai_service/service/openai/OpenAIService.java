package feo.health.ai_service.service.openai;

import com.fasterxml.jackson.databind.JsonNode;

public interface OpenAIService {
    JsonNode sendRequest(String instructions, String request);
}