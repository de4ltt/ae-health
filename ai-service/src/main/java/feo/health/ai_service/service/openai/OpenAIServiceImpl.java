package feo.health.ai_service.service.openai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAIServiceImpl implements OpenAIService {

    @Value("${openai.proxy}")
    private String proxy;

    @Value(("${openai.key}"))
    private String key;

    @Value("${openai.model}")
    private String model;

    private final RestTemplate restTemplate;

    @Override
    public JsonNode sendRequest(String instructions, String request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + key);

        Map<String, String> body = Map.of(
                "model", model,
                "instructions", instructions,
                "input", request
        );

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        String jsonResponse = restTemplate.postForObject(proxy, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String text = objectMapper.readTree(jsonResponse)
                    .get("output").get(0)
                    .get("content").get(0)
                    .get("text").asText();

            text = text
                    .replaceAll("```json\\s*", "")
                    .replaceAll("```", "")
                    .trim();

            return objectMapper.readTree(text);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
