package nl._42.springai.hackathon.chatbot;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;

/**
 * All the different chatbots implement from here, so here you can manage config globally
 */
public class DefaultClientBuilder {

    public static ChatMemory IN_MEMORY_CHAT = new InMemoryChatMemory();

    public static ChatClient.Builder addDefaults(ChatClient.Builder clientBuilder) {
        return clientBuilder.defaultAdvisors(List.of(new SimpleLoggerAdvisor(), new MessageChatMemoryAdvisor(IN_MEMORY_CHAT)));
    }
}
