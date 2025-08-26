package feo.health.ai_service.model.response;

import lombok.Data;

import java.util.List;

@Data
public class OpenAIResponse {

    private List<OutputItem> output;

    @Data
    public static class OutputItem {
        private List<ContentItem> content;

        @Data
        public static class ContentItem {
            private String text;
        }
    }
}



