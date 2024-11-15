package nl._42.springai.hackathon.chatbot.lab;

import static nl._42.springai.hackathon.chatbot.lab.SpringAITicketConfiguration.AI_FUNCTION_NAMES;

import java.util.List;
import java.util.Map;

import nl._42.springai.hackathon.chatbot.DefaultClientBuilder;
import nl._42.springai.hackathon.chatbot.SpringAIChatBot;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * If you are doing the lab assignments, start here!
 * Use examples from the 'examples' package to see different implementations
 */
@Component
@ConditionalOnProperty(value = "app.active-chatbot", havingValue = "service-desk")
public class SpringAIServiceDeskChatbot implements SpringAIChatBot<String> {

    public static final String SYSTEM_PROMPT = """
                You are an AI service desk employee.
                You will try to answer questions based on answers in previously completed tickets.
                
                The current user id = {currentId}
                
                If you cannot answer a new question, you can offer to create a new ticket for the user using the function call provided.
            """;
    private static final Long CURRENT_USER_ID = -1L; //Hard coded to make it easier for us :)

    private final ChatClient aiClient;
    private final VectorStore vectorStore;

    private final OpenAiChatOptions promptOptions = OpenAiChatOptions
            .builder()
            .withFunctions(AI_FUNCTION_NAMES)
            .build();

    public SpringAIServiceDeskChatbot(ChatClient.Builder clientBuilder, VectorStore vectorStore) {
        this.aiClient = DefaultClientBuilder.addDefaults(clientBuilder).build();
        this.vectorStore = vectorStore;
    }

    public String chat(String message) {
        Message systemMessage = new SystemPromptTemplate(SYSTEM_PROMPT).createMessage(
                Map.of("currentId", CURRENT_USER_ID)
        );

        UserMessage userMessage = new UserMessage(message);

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage), promptOptions);
        return aiClient.prompt(prompt).call().content();
    }
}
