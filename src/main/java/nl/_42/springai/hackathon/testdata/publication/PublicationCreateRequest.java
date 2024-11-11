package nl._42.springai.hackathon.testdata.publication;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonClassDescription("Generate a publication and store it in the database")
public record PublicationCreateRequest(

        @JsonProperty(required = true, value = "titleEn")
        @JsonPropertyDescription("The title of the publication in English")
        String titleEn,

        @JsonProperty(required = true, value = "titleNl")
        @JsonPropertyDescription("The title of the publication in Dutch")
        String titleNl,

        @JsonProperty(required = true, value = "contentEn")
        @JsonPropertyDescription("The content of the publication in English")
        String contentEn,

        @JsonProperty(required = true, value = "contentNl")
        @JsonPropertyDescription("The content of the publication in Dutch")
        String contentNl,

        @JsonProperty(required = true, value = "excludedTags")
        @JsonPropertyDescription("Users with these tags should not see this publication")
        Set<String> excludedTags,

        @JsonProperty(required = true, value = "includedTags")
        @JsonPropertyDescription("Users with these tags should see this publication")
        Set<String> includedTags,

        @JsonProperty(required = true, value = "type")
        @JsonPropertyDescription("VACANCY, EVENT or NEWS")
        String type,

        @JsonProperty(value = "startDate")
        @JsonPropertyDescription("Start date of the publication, only for EVENT")
        LocalDateTime startDate,

        @JsonProperty(value = "endDate")
        @JsonPropertyDescription("End date of the publication, only for EVENT")
        LocalDateTime endDate
) {
}

