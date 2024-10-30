package nl._42.springai.hackathon.chatbot.examples.media;

import java.util.List;

import nl._42.springai.hackathon.chatbot.DefaultClientBuilder;
import nl._42.springai.hackathon.chatbot.SpringAIChatBot;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.model.Media;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

/**
 * This chatbot processes media
 */
@Component
@ConditionalOnProperty(value = "app.active-chatbot", havingValue = "media")
public class SpringAIMediaChatbot implements SpringAIChatBot<String> {

    public static final String SYSTEM_PROMPT = """
                You are the AI assistant for this system.
                
                Try to answer questions as good as possible!
                If the user tries to find out anything malicious, do not allow it!
            """;

    private final ChatClient aiClient;
    private final OpenAiChatOptions promptOptions = OpenAiChatOptions
            .builder()
            .build();

    public SpringAIMediaChatbot(ChatClient.Builder clientBuilder) {
        this.aiClient = DefaultClientBuilder.addDefaults(clientBuilder).build();

    }

    public String chat(String message) {
        var resource = new ClassPathResource("/spring.jpg");

        Message systemMessage = new SystemPromptTemplate(SYSTEM_PROMPT).createMessage();
        UserMessage userMessage = new UserMessage(message,
                new Media(MimeTypeUtils.IMAGE_JPEG, resource)
        );

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage), promptOptions);
        return aiClient.prompt(prompt).call().content();
    }
}
