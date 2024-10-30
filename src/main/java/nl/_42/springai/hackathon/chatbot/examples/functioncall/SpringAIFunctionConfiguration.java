package nl._42.springai.hackathon.chatbot.examples.functioncall;

import java.util.Set;

import jakarta.persistence.EntityManager;
import nl._42.springai.hackathon.chatbot.examples.functioncall.function.classify.ClassifyReviewAiService;
import nl._42.springai.hackathon.chatbot.examples.functioncall.function.getuser.GetUserAIService;
import nl._42.springai.hackathon.chatbot.examples.functioncall.function.sqlquery.GetUserSQLAIService;
import nl._42.springai.hackathon.testdata.user.UserRepository;
import nl._42.springai.hackathon.testdata.user.UserReviewRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringAIFunctionConfiguration {

    public static final String SEARCH_USERS_AI_SERVICE = "searchUsersAiService";
    public static final String QUERY_DATA_AI_SERVICE = "queryDataAiService";
    public static final String CLASSIFY_REVIEW_AI_SERVICE = "classifyReviewAiService";

    public static final Set<String> AI_FUNCTION_NAMES = Set.of(SEARCH_USERS_AI_SERVICE, QUERY_DATA_AI_SERVICE, CLASSIFY_REVIEW_AI_SERVICE);

    @Bean
    GetUserAIService searchUsersAiService(UserRepository userRepository) {
        return new GetUserAIService(userRepository);
    }

    @Bean
    GetUserSQLAIService queryDataAiService(EntityManager entityManager) {
        return new GetUserSQLAIService(entityManager);
    }

    @Bean
    ClassifyReviewAiService classifyReviewAiService(UserReviewRepository reviewRepository) {
        return new ClassifyReviewAiService(reviewRepository);
    }

}
