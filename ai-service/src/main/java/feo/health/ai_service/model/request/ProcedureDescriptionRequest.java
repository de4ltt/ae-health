package feo.health.ai_service.model.request;

import lombok.Value;

@Value
public class ProcedureDescriptionRequest {

    String serviceName;

    @Override
    public String toString() {
        return "название процедуры: " + serviceName;
    }
}
