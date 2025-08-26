package feo.health.ai_service.service.ai;

import feo.health.ai_service.model.request.DiseaseGuessRequest;
import feo.health.ai_service.model.request.ProcedureDescriptionRequest;
import feo.health.ai_service.model.request.SuggestionRequest;
import feo.health.ai_service.model.response.DiseaseGuessResponse;
import feo.health.ai_service.model.response.ProcedureDescriptionResponse;
import feo.health.ai_service.model.response.SuggestionResponse;

public interface AIService {
    DiseaseGuessResponse getDiseaseGuess(Long userId, DiseaseGuessRequest request);
    ProcedureDescriptionResponse getProcedureDescription(Long userId, ProcedureDescriptionRequest request);
    SuggestionResponse getSuggestion(Long userId, SuggestionRequest request);
}
