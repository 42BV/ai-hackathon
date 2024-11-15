package nl._42.springai.hackathon.domain.publication;

import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringAIPublicationConfiguration {

    public static final String CREATE_PUBLICATION_FUNCTION_CALL = "createPublicationFunctionCall";

    public static final Set<String> AI_FUNCTION_NAMES = Set.of(CREATE_PUBLICATION_FUNCTION_CALL);

    @Bean
    CreatePublicationFunctionCall createPublicationFunctionCall(PublicationRepository repository) {
        return new CreatePublicationFunctionCall(repository);
    }
}
