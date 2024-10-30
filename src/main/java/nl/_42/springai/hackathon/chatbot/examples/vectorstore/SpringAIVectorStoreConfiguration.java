package nl._42.springai.hackathon.chatbot.examples.vectorstore;

import org.elasticsearch.client.RestClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.ElasticsearchVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringAIVectorStoreConfiguration {

    @Bean
    VectorStore vectorStore(RestClient client, EmbeddingModel model) {
        return new ElasticsearchVectorStore(client, model, true);
    }

    @Bean
    TokenTextSplitter tokenTextSplitter() {
        return new TokenTextSplitter();
    }

}
