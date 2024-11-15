package nl._42.springai.hackathon.chatbot.lab.create;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonClassDescription("Create a new ticket in the service desk")
public record TicketCreateRequest(

        @JsonProperty("userId")
        @JsonPropertyDescription("ID of the user that initiated the ticket")
        Long userId,

        @JsonProperty("title")
        @JsonPropertyDescription("Title of the ticket")
        String title,

        @JsonProperty("description")
        @JsonPropertyDescription("Description of the ticket")
        String description
) {
}
