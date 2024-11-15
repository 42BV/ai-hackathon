package nl._42.springai.hackathon.chatbot.examples.functioncall.function.classify;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonClassDescription("Classify a review in the database")
public record ClassifyReviewRequest(

        @JsonProperty(required = true, value = "id")
        @JsonPropertyDescription("ID of the review to update")
        long id,

        @JsonProperty(required = true, value = "score")
        @JsonPropertyDescription("Value between 1 and 10, 1 = bad, 10 = amazing. Should be based on the review field")
        long score,

        @JsonProperty(required = true, value = "subject")
        @JsonPropertyDescription("Main subject of the review")
        String subject
) {
}

