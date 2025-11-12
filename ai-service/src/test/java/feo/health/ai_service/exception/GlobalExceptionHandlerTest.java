package feo.health.ai_service.exception;

import feo.health.ai_service.controller.AIController;
import feo.health.ai_service.service.ai.AIService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AIController.class)
class GlobalExceptionHandlerTest {

    @Autowired private MockMvc mockMvc;

    @MockitoBean
    private AIService aiService;

    @Test
    void handleRuntimeException() throws Exception {
        when(aiService.getDiseaseGuess(any(), any()))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("AI failed")));

        mockMvc.perform(post("/api/v1/ai/disease")
                        .header("X-User-Id", "1")
                        .contentType("application/json")
                        .content("{\"symptoms\": [\"headache\"]}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("AI failed"));
    }

    @Test
    void handleInvalidRequest() throws Exception {
        mockMvc.perform(post("/api/v1/ai/disease")
                        .header("X-User-Id", "1")
                        .contentType("application/json")
                        .content("{\"symptoms\": null}"))
                .andExpect(status().isBadRequest());
    }
}