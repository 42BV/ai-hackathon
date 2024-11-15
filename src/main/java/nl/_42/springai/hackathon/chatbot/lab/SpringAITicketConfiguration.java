package nl._42.springai.hackathon.chatbot.lab;

import java.util.Set;

import nl._42.springai.hackathon.chatbot.lab.create.TicketCreateFunctionCall;
import nl._42.springai.hackathon.domain.ticket.TicketRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringAITicketConfiguration {

    public static final String CREATE_TICKET_FUNCTION_CALL = "createTicketFunctionCall";

    public static final Set<String> AI_FUNCTION_NAMES = Set.of(CREATE_TICKET_FUNCTION_CALL);

    @Bean
    TicketCreateFunctionCall createTicketFunctionCall(TicketRepository repository) {
        return new TicketCreateFunctionCall(repository);
    }
}
