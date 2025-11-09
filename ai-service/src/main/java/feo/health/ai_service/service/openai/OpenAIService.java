package feo.health.ai_service.service.openai;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.concurrent.CompletableFuture;

public interface OpenAIService {
    CompletableFuture<JsonNode> sendRequest(String instructions, String request);
}
