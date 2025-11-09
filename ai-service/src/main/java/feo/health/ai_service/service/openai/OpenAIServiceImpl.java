package feo.health.ai_service.service.openai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class OpenAIServiceImpl implements OpenAIService {

    @Value("${openai.proxy}")
    private String proxy;

    @Value("${openai.key}")
    private String key;

    @Value("${openai.model}")
    private String model;

    private final WebClient webClient;
    private final ObjectMapper mapper = new ObjectMapper();

    @Async
    @Override
    public CompletableFuture<JsonNode> sendRequest(String instructions, String request) {

        Map<String, String> body = Map.of(
                "model", model,
                "instructions", instructions,
                "input", request
        );

        return webClient.post()
                .uri(proxy)
                .header("Authorization", "Bearer " + key)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .toFuture()
                .thenApply(json -> {
                    try {
                        String text = mapper.readTree(json)
                                .get("output").get(0)
                                .get("content").get(0)
                                .get("text").asText()
                                .replaceAll("```json\\s*", "")
                                .replaceAll("```", "")
                                .trim();

                        return mapper.readTree(text);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
