package nl._42.springai.hackathon.chatbot.examples.functioncall.function.sqlquery;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonClassDescription("Perform actions on data using a raw SQL query")
public record UserSqlRequest(
        @JsonProperty(required = true, value = "query")
        @JsonPropertyDescription("""
                This query is a native PostgreSQL to execute.
                                                                
                The schema is as follows:
                                
                app_user table:
                        id: bigint
                        name: varchar(150)
                        date_of_birth: date
                        age: integer
                                
                user_activity table:
                        id: bigint
                        action: varchar(64)
                        action_timestamp timestamp
                        user_id bigint -> FK to app_user.id
                        
                user_review table:
                        id: bigint
                        timestamp: timestamp
                        user_id: bigint
                        review: varchar(2000)
                        score: int
                        subject: varchar(256)
                        
                publication table:
                         id: bigint
                         title: jsonb -> structure as follows: {"en": "English title", "nl": "Nederlandse titel"}
                         content: jsonb -> structure as follows: {"en": "English content", "nl": "Nederlandse content"}
                         type: varchar(256) -> 0, 1, 2
                         included_tags: jsonb -> structure as follows: ["ENVIRONMENTALIST", "TECH_ENTHUSIAST", "CAR_OWNER"]
                         excluded_tags: jsonb -> structure as follows: ["ENVIRONMENTALIST", "TECH_ENTHUSIAST", "CAR_OWNER"]
                         start_date: timestamp
                         end_date: timestamp
                         keywords: varchar(256)
                         
                 ticket table:
                        id: bigint
                        user_id bigint -> FK to app_user.id
                        title: varchar(255)
                        created_at: timestamp
                        comments: jsonb -> structure as follows: ["Comment 1", "Comment2"]
                        description: text
                        completed: boolean
                """)
        String query
) {
}

