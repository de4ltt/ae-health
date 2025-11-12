package feo.health.ai_service.service.ai;

import feo.health.ai_service.model.request.DiseaseGuessRequest;
import feo.health.ai_service.model.request.ProcedureDescriptionRequest;
import feo.health.ai_service.model.request.SuggestionRequest;
import feo.health.ai_service.model.response.DiseaseGuessResponse;
import feo.health.ai_service.model.response.ProcedureDescriptionResponse;
import feo.health.ai_service.model.response.SuggestionResponse;
import feo.health.ai_service.service.openai.OpenAIService;
import feo.health.ai_service.service.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class AIServiceImpl implements AIService {

    private final OpenAIService openAIService;
    private final UserService userService;

    @Value("${prompts.disease-guess}")
    private final String DISEASE_GUESS_REQUEST;

    @Value("${prompts.procedure-description}")
    private final String PROCEDURE_DESCRIPTION_REQUEST;

    @Value("${prompts.suggestions}")
    private final String SUGGESTIONS_REQUEST;

    @Async
    @Override
    public CompletableFuture<DiseaseGuessResponse> getDiseaseGuess(Long userId, DiseaseGuessRequest req) {
        return userService.getUserParamsById(userId)
                .thenCompose(params -> {
                    String input = req + ". " + params;
                    return openAIService.sendRequest(DISEASE_GUESS_REQUEST, input);
                })
                .thenApply(DiseaseGuessResponse::from);
    }

    @Async
    @Override
    public CompletableFuture<ProcedureDescriptionResponse> getProcedureDescription(
            Long userId,
            ProcedureDescriptionRequest req
    ) {
        return userService.getUserParamsById(userId)
                .thenCompose(params -> {
                    String input = req + ". " + params;
                    return openAIService.sendRequest(PROCEDURE_DESCRIPTION_REQUEST, input);
                })
                .thenApply(ProcedureDescriptionResponse::from);
    }

    @Async
    @Override
    public CompletableFuture<SuggestionResponse> getSuggestion(Long userId, SuggestionRequest req) {
        return userService.getUserParamsById(userId)
                .thenCompose(params -> {
                    String input = req + ". " + params;
                    return openAIService.sendRequest(SUGGESTIONS_REQUEST, input);
                })
                .thenApply(SuggestionResponse::from);
    }
}
