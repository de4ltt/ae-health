package feo.health.ai_service.service.ai;

import feo.health.ai_service.model.request.DiseaseGuessRequest;
import feo.health.ai_service.model.request.ProcedureDescriptionRequest;
import feo.health.ai_service.model.request.SuggestionRequest;
import feo.health.ai_service.model.response.DiseaseGuessResponse;
import feo.health.ai_service.model.response.ProcedureDescriptionResponse;
import feo.health.ai_service.model.response.SuggestionResponse;

import java.util.concurrent.CompletableFuture;

public interface AIService {
    CompletableFuture<DiseaseGuessResponse> getDiseaseGuess(Long userId, DiseaseGuessRequest req);
    CompletableFuture<ProcedureDescriptionResponse> getProcedureDescription(Long userId, ProcedureDescriptionRequest req);
    CompletableFuture<SuggestionResponse> getSuggestion(Long userId, SuggestionRequest req);
}

