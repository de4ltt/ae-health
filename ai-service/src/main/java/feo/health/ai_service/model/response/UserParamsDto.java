package feo.health.ai_service.model.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserParamsDto {

    int age;
    Integer heightCm;
    Double weightKg;

    @Override
    public String toString() {
        return "Возраст: " + age +
                ", рост: " + (heightCm != null ? heightCm : "нет данных") +
                ", вес: " + (weightKg != null ? weightKg : "нет данных");
    }
}
