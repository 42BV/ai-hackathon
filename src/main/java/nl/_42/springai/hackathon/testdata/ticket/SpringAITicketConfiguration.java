package nl._42.springai.hackathon.testdata.ticket;

import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringAITicketConfiguration {

    public static final String CREATE_PUBLICATION_FUNCTION_CALL = "createTicketFunctionCall";

    public static final Set<String> AI_FUNCTION_NAMES = Set.of(CREATE_PUBLICATION_FUNCTION_CALL);

    @Bean
    CreateTicketFunctionCall createTicketFunctionCall(TicketRepository repository) {
        return new CreateTicketFunctionCall(repository);
    }
}
