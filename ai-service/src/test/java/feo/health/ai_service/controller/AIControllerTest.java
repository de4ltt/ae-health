package feo.health.ai_service.controller;

import feo.health.ai_service.model.request.DiseaseGuessRequest;
import feo.health.ai_service.model.request.ProcedureDescriptionRequest;
import feo.health.ai_service.model.request.SuggestionRequest;
import feo.health.ai_service.model.response.DiseaseGuessResponse;
import feo.health.ai_service.model.response.ProcedureDescriptionResponse;
import feo.health.ai_service.model.response.SuggestionResponse;
import feo.health.ai_service.service.ai.AIService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AIControllerTest {

    @Mock
    private AIService aiService;

    @InjectMocks
    private AIController aiController;

    @Test
    void getDiseaseGuess_success() throws Exception {
        Long userId = 1L;
        DiseaseGuessRequest req = new DiseaseGuessRequest(List.of("symptom1"));
        DiseaseGuessResponse response = DiseaseGuessResponse.builder()
                .possibleDiseases(Map.of("Disease1", 0.8f))
                .doctors(List.of("Doctor1"))
                .generalResponse("Response")
                .build();

        when(aiService.getDiseaseGuess(userId, req)).thenReturn(CompletableFuture.completedFuture(response));

        CompletableFuture<ResponseEntity<DiseaseGuessResponse>> future = aiController.getDiseaseGuess(userId, req);
        ResponseEntity<DiseaseGuessResponse> result = future.get();

        assertNotNull(result);
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(response, result.getBody());
    }

    @Test
    void getDiseaseGuess_failure() {
        Long userId = 1L;
        DiseaseGuessRequest req = new DiseaseGuessRequest(Collections.emptyList());

        CompletableFuture<DiseaseGuessResponse> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("AI error"));

        when(aiService.getDiseaseGuess(userId, req)).thenReturn(failedFuture);

        CompletableFuture<ResponseEntity<DiseaseGuessResponse>> future = aiController.getDiseaseGuess(userId, req);

        ExecutionException exception = assertThrows(ExecutionException.class, future::get);
        assertInstanceOf(RuntimeException.class, exception.getCause());
        assertEquals("AI error", exception.getCause().getMessage());
    }

    @Test
    void getSuggestion_success() throws Exception {
        Long userId = 1L;
        SuggestionRequest req = new SuggestionRequest("Input");
        SuggestionResponse response = SuggestionResponse.builder()
                .doctors(List.of("DoctorA"))
                .drugs(List.of("Drug1"))
                .possibleDiseases(Map.of("DiseaseA", 0.7f))
                .generalAnswer("Answer")
                .build();

        when(aiService.getSuggestion(userId, req)).thenReturn(CompletableFuture.completedFuture(response));

        CompletableFuture<ResponseEntity<SuggestionResponse>> future = aiController.getSuggestion(userId, req);
        ResponseEntity<SuggestionResponse> result = future.get();

        assertNotNull(result);
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(response, result.getBody());
    }

    @Test
    void getSuggestion_failure() {
        Long userId = 1L;
        SuggestionRequest req = new SuggestionRequest("Input");

        CompletableFuture<SuggestionResponse> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("AI error"));

        when(aiService.getSuggestion(userId, req)).thenReturn(failedFuture);

        CompletableFuture<ResponseEntity<SuggestionResponse>> future = aiController.getSuggestion(userId, req);

        ExecutionException exception = assertThrows(ExecutionException.class, future::get);
        assertInstanceOf(RuntimeException.class, exception.getCause());
        assertEquals("AI error", exception.getCause().getMessage());
    }

    @Test
    void getProcedureDescription_success() throws Exception {
        Long userId = 1L;
        ProcedureDescriptionRequest req = new ProcedureDescriptionRequest("Procedure");
        ProcedureDescriptionResponse response = ProcedureDescriptionResponse.builder()
                .name("Procedure")
                .description("Desc")
                .contradictions(List.of("Contra1"))
                .indications(List.of("Indi1"))
                .build();

        when(aiService.getProcedureDescription(userId, req)).thenReturn(CompletableFuture.completedFuture(response));

        CompletableFuture<ResponseEntity<ProcedureDescriptionResponse>> future = aiController.getProcedureDescription(userId, req);
        ResponseEntity<ProcedureDescriptionResponse> result = future.get();

        assertNotNull(result);
        assertTrue(result.getStatusCode().is2xxSuccessful());
        assertEquals(response, result.getBody());
    }

    @Test
    void getProcedureDescription_failure() {
        Long userId = 1L;
        ProcedureDescriptionRequest req = new ProcedureDescriptionRequest("Procedure");

        CompletableFuture<ProcedureDescriptionResponse> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("AI error"));

        when(aiService.getProcedureDescription(userId, req)).thenReturn(failedFuture);

        CompletableFuture<ResponseEntity<ProcedureDescriptionResponse>> future = aiController.getProcedureDescription(userId, req);

        ExecutionException exception = assertThrows(ExecutionException.class, future::get);
        assertInstanceOf(RuntimeException.class, exception.getCause());
        assertEquals("AI error", exception.getCause().getMessage());
    }
}