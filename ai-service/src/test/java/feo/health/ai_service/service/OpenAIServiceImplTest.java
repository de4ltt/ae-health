package feo.health.ai_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feo.health.ai_service.service.openai.OpenAIServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenAIServiceImplTest {

    @Mock
    private WebClient webClient;

    @InjectMocks
    private OpenAIServiceImpl openAIService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(openAIService, "proxy", "http://proxy");
        ReflectionTestUtils.setField(openAIService, "key", "api-key");
        ReflectionTestUtils.setField(openAIService, "model", "gpt-model");
    }

    @Test
    void sendRequest_success() throws Exception {
        String instructions = "Test instructions";
        String request = "Test request";

        String rawResponse = "{\"output\":[{\"content\":[{\"text\":\"```json\\n{\\\"key\\\":\\\"value\\\"}\\n```\"}]}]}";
        JsonNode expected = objectMapper.readTree("{\"key\":\"value\"}");

        WebClient.RequestBodyUriSpec uriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec bodySpec = mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.post()).thenReturn(uriSpec);
        when(uriSpec.uri("http://proxy")).thenReturn(bodySpec);
        when(bodySpec.header("Authorization", "Bearer api-key")).thenReturn(bodySpec);
        when(bodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(bodySpec);
        when(bodySpec.bodyValue(ArgumentMatchers.<Map<String, String>>any())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(rawResponse));

        CompletableFuture<JsonNode> future = openAIService.sendRequest(instructions, request);
        JsonNode result = future.get();

        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    void sendRequest_parsingFailure() {
        String instructions = "Test instructions";
        String request = "Test request";

        String badResponse = "{\"output\":[{\"content\":[{\"text\":\"bad json\"}]}]}";

        WebClient.RequestBodyUriSpec uriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec bodySpec = mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.post()).thenReturn(uriSpec);
        when(uriSpec.uri("http://proxy")).thenReturn(bodySpec);
        when(bodySpec.header("Authorization", "Bearer api-key")).thenReturn(bodySpec);
        when(bodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(bodySpec);
        when(bodySpec.bodyValue(any(Map.class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(badResponse));

        CompletableFuture<JsonNode> future = openAIService.sendRequest(instructions, request);

        ExecutionException exception = assertThrows(ExecutionException.class, future::get);
        assertInstanceOf(RuntimeException.class, exception.getCause());
    }

    @Test
    void sendRequest_webClientFailure() {
        String instructions = "Test instructions";
        String request = "Test request";

        WebClient.RequestBodyUriSpec uriSpec = mock(WebClient.RequestBodyUriSpec.class);
        WebClient.RequestBodySpec bodySpec = mock(WebClient.RequestBodySpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.post()).thenReturn(uriSpec);
        when(uriSpec.uri("http://proxy")).thenReturn(bodySpec);
        when(bodySpec.header("Authorization", "Bearer api-key")).thenReturn(bodySpec);
        when(bodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(bodySpec);
        when(bodySpec.bodyValue(any(Map.class))).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.error(new RuntimeException("WebClient error")));

        CompletableFuture<JsonNode> future = openAIService.sendRequest(instructions, request);

        ExecutionException exception = assertThrows(ExecutionException.class, future::get);
        assertInstanceOf(RuntimeException.class, exception.getCause());
        assertEquals("WebClient error", exception.getCause().getMessage());
    }
}