package nl._42.springai.hackathon.testdata.ticket;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonClassDescription("Generate a ticket, and store it in the database.")
public record TicketCreateRequest(

        @JsonProperty("title")
        @JsonPropertyDescription("Title of the ticket.")
        String title,

        @JsonProperty("description")
        @JsonPropertyDescription("Description of the ticket.")
        String description,

        @JsonProperty("createdAt")
        @JsonPropertyDescription("The timestamp of the moment when this ticket was created.")
        LocalDateTime createdAt,

        @JsonProperty("comments")
        @JsonPropertyDescription("Array of comments, related to this ticket.")
        Set<Comment> comments
) {
}
