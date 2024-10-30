package nl._42.springai.hackathon.chatbot.examples.functioncall.function.getuser;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonClassDescription("Search users using specified fields")
public record UserRequest(
        @JsonProperty(required = true, value = "name")
        @JsonPropertyDescription("The name or part of the name of the users")
        String name,

        @JsonProperty(required = true, value = "dateOfBirth")
        @JsonPropertyDescription("Date of birth of the user")
        LocalDate dateOfBirth
) {
}

