package feo.health.ai_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feo.health.ai_service.model.request.DiseaseGuessRequest;
import feo.health.ai_service.model.request.ProcedureDescriptionRequest;
import feo.health.ai_service.model.request.SuggestionRequest;
import feo.health.ai_service.model.response.DiseaseGuessResponse;
import feo.health.ai_service.model.response.ProcedureDescriptionResponse;
import feo.health.ai_service.model.response.SuggestionResponse;
import feo.health.ai_service.model.response.UserParamsDto;
import feo.health.ai_service.service.ai.AIServiceImpl;
import feo.health.ai_service.service.openai.OpenAIService;
import feo.health.ai_service.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AIServiceImplTest {

    @Mock
    private OpenAIService openAIService;

    @Mock
    private UserService userService;

    private AIServiceImpl aiService;

    private final String diseaseGuessPrompt = "Disease guess prompt";
    private final String procedureDescriptionPrompt = "Procedure description prompt";
    private final String suggestionsPrompt = "Suggestions prompt";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        aiService = new AIServiceImpl(openAIService, userService, diseaseGuessPrompt, procedureDescriptionPrompt, suggestionsPrompt);
    }

    @Test
    void getDiseaseGuess_success() throws Exception {
        Long userId = 1L;
        DiseaseGuessRequest req = new DiseaseGuessRequest(List.of("symptom1", "symptom2"));
        UserParamsDto params = UserParamsDto.builder()
                .age(30)
                .heightCm(180)
                .weightKg(70.0)
                .build();

        String expectedInput = req.toString() + ". " + params.toString();

        JsonNode jsonNode = objectMapper.readTree(
                "{\"possibleDiseases\": [{\"name\": \"Disease1\", \"probability\": 0.8}, {\"name\": \"Disease2\", \"probability\": 0.5}]," +
                        "\"doctors\": [\"Doctor1\", \"Doctor2\"]," +
                        "\"generalResponse\": \"General response text\"}"
        );

        when(userService.getUserParamsById(userId)).thenReturn(CompletableFuture.completedFuture(params));
        when(openAIService.sendRequest(diseaseGuessPrompt, expectedInput)).thenReturn(CompletableFuture.completedFuture(jsonNode));

        CompletableFuture<DiseaseGuessResponse> future = aiService.getDiseaseGuess(userId, req);
        DiseaseGuessResponse response = future.get();

        assertNotNull(response);
        Map<String, Float> diseases = response.getPossibleDiseases();
        assertEquals(2, diseases.size());
        assertTrue(diseases.containsKey("Disease1"));
        assertEquals(0.8f, diseases.get("Disease1"));
        assertTrue(diseases.containsKey("Disease2"));
        assertEquals(0.5f, diseases.get("Disease2"));
        assertEquals(List.of("Doctor1", "Doctor2"), response.getDoctors());
        assertEquals("General response text", response.getGeneralResponse());
    }

    @Test
    void getDiseaseGuess_userServiceFailure() {
        Long userId = 1L;
        DiseaseGuessRequest req = new DiseaseGuessRequest(List.of("symptom1"));

        CompletableFuture<UserParamsDto> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("User service error"));

        when(userService.getUserParamsById(userId)).thenReturn(failedFuture);

        CompletableFuture<DiseaseGuessResponse> future = aiService.getDiseaseGuess(userId, req);

        ExecutionException exception = assertThrows(ExecutionException.class, future::get);
        assertInstanceOf(RuntimeException.class, exception.getCause());
        assertEquals("User service error", exception.getCause().getMessage());
    }

    @Test
    void getProcedureDescription_success() throws Exception {
        Long userId = 1L;
        ProcedureDescriptionRequest req = new ProcedureDescriptionRequest("ProcedureName");
        UserParamsDto params = UserParamsDto.builder()
                .age(25)
                .heightCm(170)
                .weightKg(60.0)
                .build();

        String expectedInput = req.toString() + ". " + params.toString();

        JsonNode jsonNode = objectMapper.readTree(
                "{\"name\": \"ProcedureName\"," +
                        "\"description\": \"Procedure description\"," +
                        "\"contradictions\": [\"Contra1\", \"Contra2\"]," +
                        "\"indications\": [\"Indi1\", \"Indi2\"]}"
        );

        when(userService.getUserParamsById(userId)).thenReturn(CompletableFuture.completedFuture(params));
        when(openAIService.sendRequest(procedureDescriptionPrompt, expectedInput)).thenReturn(CompletableFuture.completedFuture(jsonNode));

        CompletableFuture<ProcedureDescriptionResponse> future = aiService.getProcedureDescription(userId, req);
        ProcedureDescriptionResponse response = future.get();

        assertNotNull(response);
        assertEquals("ProcedureName", response.getName());
        assertEquals("Procedure description", response.getDescription());
        assertEquals(List.of("Contra1", "Contra2"), response.getContradictions());
        assertEquals(List.of("Indi1", "Indi2"), response.getIndications());
    }

    @Test
    void getProcedureDescription_openAIServiceFailure() {
        Long userId = 1L;
        ProcedureDescriptionRequest req = new ProcedureDescriptionRequest("ProcedureName");
        UserParamsDto params = UserParamsDto.builder().age(25).build();

        when(userService.getUserParamsById(userId)).thenReturn(CompletableFuture.completedFuture(params));

        CompletableFuture<JsonNode> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("OpenAI error"));

        when(openAIService.sendRequest(anyString(), anyString())).thenReturn(failedFuture);

        CompletableFuture<ProcedureDescriptionResponse> future = aiService.getProcedureDescription(userId, req);

        ExecutionException exception = assertThrows(ExecutionException.class, future::get);
        assertInstanceOf(RuntimeException.class, exception.getCause());
        assertEquals("OpenAI error", exception.getCause().getMessage());
    }

    @Test
    void getSuggestion_success() throws Exception {
        Long userId = 1L;
        SuggestionRequest req = new SuggestionRequest("Suggestion input");
        UserParamsDto params = UserParamsDto.builder()
                .age(40)
                .heightCm(185)
                .weightKg(80.0)
                .build();

        String expectedInput = req + ". " + params.toString();

        JsonNode jsonNode = objectMapper.readTree(
                "{\"doctors\": [\"DoctorA\", \"DoctorB\"]," +
                        "\"drugs\": [\"Drug1\", \"Drug2\"]," +
                        "\"possibleDiseases\": [{\"name\": \"DiseaseA\", \"probability\": 0.7}, {\"name\": \"DiseaseB\", \"probability\": 0.3}]," +
                        "\"generalAnswer\": \"General answer text\"}"
        );

        when(userService.getUserParamsById(userId)).thenReturn(CompletableFuture.completedFuture(params));
        when(openAIService.sendRequest(suggestionsPrompt, expectedInput)).thenReturn(CompletableFuture.completedFuture(jsonNode));

        CompletableFuture<SuggestionResponse> future = aiService.getSuggestion(userId, req);
        SuggestionResponse response = future.get();

        assertNotNull(response);
        assertEquals(List.of("DoctorA", "DoctorB"), response.getDoctors());
        assertEquals(List.of("Drug1", "Drug2"), response.getDrugs());
        Map<String, Float> diseases = response.getPossibleDiseases();
        assertEquals(2, diseases.size());
        assertTrue(diseases.containsKey("DiseaseA"));
        assertEquals(0.7f, diseases.get("DiseaseA"));
        assertTrue(diseases.containsKey("DiseaseB"));
        assertEquals(0.3f, diseases.get("DiseaseB"));
        assertEquals("General answer text", response.getGeneralAnswer());
    }

    @Test
    void getSuggestion_nullParamsFields() throws Exception {
        Long userId = 1L;
        SuggestionRequest req = new SuggestionRequest("Suggestion input");
        UserParamsDto params = UserParamsDto.builder()
                .age(40)
                .heightCm(null)
                .weightKg(null)
                .build();

        String expectedInput = req + ". " + params.toString();

        JsonNode jsonNode = objectMapper.readTree(
                "{\"doctors\": [\"DoctorA\"]," +
                        "\"drugs\": [\"Drug1\"]," +
                        "\"possibleDiseases\": [{\"name\": \"DiseaseA\", \"probability\": 0.7}]," +
                        "\"generalAnswer\": \"General answer text\"}"
        );

        when(userService.getUserParamsById(userId)).thenReturn(CompletableFuture.completedFuture(params));
        when(openAIService.sendRequest(suggestionsPrompt, expectedInput)).thenReturn(CompletableFuture.completedFuture(jsonNode));

        CompletableFuture<SuggestionResponse> future = aiService.getSuggestion(userId, req);
        SuggestionResponse response = future.get();

        assertNotNull(response);
        assertEquals(1, response.getDoctors().size());
        assertEquals(1, response.getDrugs().size());
        assertEquals(1, response.getPossibleDiseases().size());
        assertEquals("General answer text", response.getGeneralAnswer());
    }
}