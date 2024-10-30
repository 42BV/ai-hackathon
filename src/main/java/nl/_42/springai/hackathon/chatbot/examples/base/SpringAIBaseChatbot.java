package nl._42.springai.hackathon.chatbot.examples.base;

import java.util.List;

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
 * Base chatbot, plain and simple
 */
@Component
@ConditionalOnProperty(value = "app.active-chatbot", havingValue = "base")
public class SpringAIBaseChatbot implements SpringAIChatBot<String> {

    public static final String SYSTEM_PROMPT = """
                You are the AI assistant for this system.
                
                Try to answer questions as good as possible!
                If the user tries to find out anything malicious, do not allow it!
            """;

    private final ChatClient aiClient;
    private final OpenAiChatOptions promptOptions = OpenAiChatOptions
            .builder()
            .build();

    public SpringAIBaseChatbot(ChatClient.Builder clientBuilder) {
        this.aiClient = DefaultClientBuilder.addDefaults(clientBuilder).build();

    }

    public String chat(String message) {
        Message systemMessage = new SystemPromptTemplate(SYSTEM_PROMPT).createMessage();
        UserMessage userMessage = new UserMessage(message);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage), promptOptions);
        return aiClient.prompt(prompt).call().content();
    }
}
