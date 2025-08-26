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

@RestController
@RequestMapping("/api/v1/ai")
@AllArgsConstructor
public class AIController {

    private final AIService aiService;

    @GetMapping("/disease")
    ResponseEntity<DiseaseGuessResponse> getDiseaseGuess(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody DiseaseGuessRequest request
    ) { return ResponseEntity.ok(aiService.getDiseaseGuess(userId, request)); }

    @GetMapping("/suggestion")
    ResponseEntity<SuggestionResponse> getSuggestion(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody SuggestionRequest request
    ) { return ResponseEntity.ok(aiService.getSuggestion(userId, request)); }

    @GetMapping("/procedure")
    ResponseEntity<ProcedureDescriptionResponse> getProcedureDescription(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody ProcedureDescriptionRequest request
    ) { return ResponseEntity.ok(aiService.getProcedureDescription(userId, request)); }
}
