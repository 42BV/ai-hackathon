package nl._42.springai.hackathon.chatbot.examples.vectorstore;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import nl._42.springai.hackathon.chatbot.DefaultClientBuilder;
import nl._42.springai.hackathon.chatbot.SpringAIChatBot;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * This chatbot uses a vector store in order to get smaller chunks of related content.
 * The vector store allows for searching of related data semantically
 * Then it performs 'prompt stuffing' and adds these chunks of related content to the prompt.
 */
@Component
@ConditionalOnProperty(value = "app.active-chatbot", havingValue = "vector-store")
@Slf4j
public class SpringAIVectorStoreChatBot implements SpringAIChatBot<String> {

    private static final String SYSTEM_PROMPT = """
             You are a tool that has access to publications. Users can ask questions about them!
             
             DOCUMENTS:
             {documents}
            """;
    private final ChatClient aiClient;
    private final VectorStore vectorStore;
    public OpenAiChatOptions promptOptions = OpenAiChatOptions
            .builder()
            .build();

    public SpringAIVectorStoreChatBot(ChatClient.Builder clientBuilder, VectorStore vectorStore) {
        this.aiClient = DefaultClientBuilder.addDefaults(clientBuilder).build();
        this.vectorStore = vectorStore;
    }

    /**
     * An alternative way of doing this is registering a {@link org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor}
     * <a href="https://spring.io/blog/2024/10/02/supercharging-your-ai-applications-with-spring-ai-advisors">...</a>
     */
    public String chat(String message) {
        List<Document> listOfSimilarDocuments = this.vectorStore.similaritySearch(message).stream().toList();
        String documents = listOfSimilarDocuments
                .stream()
                //Maybe you want to use metadata here :)
                .map(Document::getContent)
                .collect(Collectors.joining("\n\n"));

        Message systemMessage = new SystemPromptTemplate(SYSTEM_PROMPT)
                .createMessage(Map.of("documents", documents));

        UserMessage userMessage = new UserMessage(message);

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage), promptOptions);
        return aiClient.prompt(prompt).call().content();
    }
}
