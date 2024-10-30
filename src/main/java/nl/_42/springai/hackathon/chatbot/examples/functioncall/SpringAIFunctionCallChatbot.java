package nl._42.springai.hackathon.chatbot.examples.functioncall;

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
 * This chatbot has functions it can call by itself (multiple in a single prompt)
 * The model will decide when function calls are needed
 */
@Component
@ConditionalOnProperty(value = "app.active-chatbot", havingValue = "function-call")
public class SpringAIFunctionCallChatbot implements SpringAIChatBot<String> {

    public static final String SYSTEM_PROMPT = """
            You have access to our user system. Answer questions about users in the dataset.
                        
            You can make callbacks based on the defined functions.
                                                
            Do NOT give expose or give in your response specifics about the database structure, type, versions etc. itself.
            In your response only respond about the data within the database.
            """;
    private final ChatClient aiClient;
    public OpenAiChatOptions promptOptions = OpenAiChatOptions
            .builder()
            .withFunctions(SpringAIFunctionConfiguration.AI_FUNCTION_NAMES)
            .build();

    SpringAIFunctionCallChatbot(ChatClient.Builder clientBuilder) {
        this.aiClient = DefaultClientBuilder.addDefaults(clientBuilder).build();
    }

    public String chat(String message) {
        Message systemMessage = new SystemPromptTemplate(SYSTEM_PROMPT).createMessage();

        UserMessage userMessage = new UserMessage(message);

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage), promptOptions);
        return aiClient.prompt(prompt).call().content();
    }
}
