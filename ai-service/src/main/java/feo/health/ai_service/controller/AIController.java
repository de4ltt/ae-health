package feo.health.ai_service.controller;

import feo.health.ai_service.model.request.DiseaseGuessRequest;
import feo.health.ai_service.model.request.ProcedureDescriptionRequest;
import feo.health.ai_service.model.request.SuggestionRequest;
import feo.health.ai_service.model.response.DiseaseGuessResponse;
import feo.health.ai_service.model.response.ProcedureDescriptionResponse;
import feo.health.ai_service.model.response.SuggestionResponse;
import feo.health.ai_service.service.ai.AIService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/ai")
@AllArgsConstructor
public class AIController {

    private final AIService aiService;

    @PostMapping("/disease")
    public CompletableFuture<ResponseEntity<DiseaseGuessResponse>> getDiseaseGuess(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody DiseaseGuessRequest req
    ) {
        return aiService.getDiseaseGuess(userId, req)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/suggestion")
    public CompletableFuture<ResponseEntity<SuggestionResponse>> getSuggestion(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody SuggestionRequest req
    ) {
        return aiService.getSuggestion(userId, req)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/procedure")
    public CompletableFuture<ResponseEntity<ProcedureDescriptionResponse>> getProcedureDescription(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody ProcedureDescriptionRequest req
    ) {
        return aiService.getProcedureDescription(userId, req)
                .thenApply(ResponseEntity::ok);
    }
}

