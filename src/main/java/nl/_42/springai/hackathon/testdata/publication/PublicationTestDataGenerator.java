package nl._42.springai.hackathon.testdata.publication;

import static nl._42.springai.hackathon.testdata.publication.SpringAIPublicationConfiguration.AI_FUNCTION_NAMES;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nl._42.springai.hackathon.chatbot.DefaultClientBuilder;
import nl._42.springai.hackathon.chatbot.SpringAIChatBot;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * This chatbot responds according to a JSON schema. It will ALWAYS adhere to the schema.
 * {@link '<a href="https://spring.io/blog/2024/08/09/spring-ai-embraces-openais-structured-outputs-enhancing-json-response">...</a>'}
 */
@Component
@ConditionalOnProperty(value = "app.active-chatbot", havingValue = "publication-generator")
public class PublicationTestDataGenerator implements SpringAIChatBot<String> {

    public static final String SYSTEM_PROMPT = """
                You are a publication generator.
                
                Create one of three types of publication: VACANCY, NEWS, EVENT.
                
                You can use the provided function calls to create the actual publication.
                
                Try to generate titles in a single sentence, and 10 sentences for content.
                
                Can you put spelling mistakes and inconsistencies, since this is test data. Mistakes in translation between dutch and english.
                
                Please note that some tags are mutually exclusive, create the publication accordingly. You can also come up with your own tags.
                
                These are the predefined tags:
                {tags}
            """;

    private final ChatClient aiClient;
    private final OpenAiChatOptions promptOptions = OpenAiChatOptions
            .builder()
            .withFunctions(AI_FUNCTION_NAMES)
            .build();

    public PublicationTestDataGenerator(ChatClient.Builder clientBuilder) {
        this.aiClient = DefaultClientBuilder.addDefaults(clientBuilder).build();

    }

    public String chat(String message) {
        Message systemMessage = new SystemPromptTemplate(SYSTEM_PROMPT)
                .createMessage(Map.of("tags", Arrays.stream(Tag.values()).map(Enum::toString).collect(Collectors.joining(","))));
        UserMessage userMessage = new UserMessage(message);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage), promptOptions);
        return aiClient.prompt(prompt).call().content();
    }
}
