package feo.health.ai_service.service.ai;

import feo.health.ai_service.model.request.DiseaseGuessRequest;
import feo.health.ai_service.model.request.ProcedureDescriptionRequest;
import feo.health.ai_service.model.request.SuggestionRequest;
import feo.health.ai_service.model.response.DiseaseGuessResponse;
import feo.health.ai_service.model.response.ProcedureDescriptionResponse;
import feo.health.ai_service.model.response.SuggestionResponse;
import feo.health.ai_service.model.response.UserParamsDto;
import feo.health.ai_service.service.openai.OpenAIService;
import feo.health.ai_service.service.user.UserService;
import feo.health.ai_service.util.AIRequestStrings;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AIServiceImpl implements AIService {

    private final OpenAIService openAIService;
    private final UserService userService;

    @Override
    public DiseaseGuessResponse getDiseaseGuess(Long userId, DiseaseGuessRequest diseaseGuessRequest) {
        UserParamsDto userParamsDto = userService.getUserParamsById(userId);
        String request = String
                .join(". ", List.of(diseaseGuessRequest.toString(), userParamsDto.toString()));
        return DiseaseGuessResponse
                .from(openAIService.sendRequest(AIRequestStrings.DISEASE_GUESS_REQUEST, request));
    }

    @Override
    public ProcedureDescriptionResponse getProcedureDescription(
            Long userId,
            ProcedureDescriptionRequest procedureDescriptionRequest
    ) {
        UserParamsDto userParamsDto = userService.getUserParamsById(userId);
        String request = String
                .join(". ", List.of(procedureDescriptionRequest.toString(), userParamsDto.toString()));
        return ProcedureDescriptionResponse
                .from(openAIService.sendRequest(AIRequestStrings.PROCEDURE_DESCRIPTION_REQUEST, request));
    }

    @Override
    public SuggestionResponse getSuggestion(Long userId, SuggestionRequest suggestionRequest) {
        UserParamsDto userParamsDto = userService.getUserParamsById(userId);
        String request = String
                .join(". ", List.of(suggestionRequest.toString(), userParamsDto.toString()));
        return SuggestionResponse
                .from(openAIService.sendRequest(AIRequestStrings.SUGGESTIONS_REQUEST, request));
    }
}
