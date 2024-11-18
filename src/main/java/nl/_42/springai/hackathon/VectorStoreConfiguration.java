package nl._42.springai.hackathon;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.ElasticsearchVectorStore;
import org.springframework.ai.vectorstore.ElasticsearchVectorStoreOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VectorStoreConfiguration {

    @Bean
    public RestClient restClient(
            @Value("${app.elasticsearch.api-key:}") String apiKey,
            @Value("${app.elasticsearch.host}") String host,
            @Value("${app.elasticsearch.protocol}") String protocol,
            @Value("${app.elasticsearch.port}") int port
    ) {
        return RestClient.builder(new HttpHost(host, port, protocol))
                .setDefaultHeaders(new BasicHeader[] { new BasicHeader(HttpHeaders.AUTHORIZATION, "ApiKey " + apiKey) })
                .build();
    }

    @Bean
    public ElasticsearchVectorStore vectorStore(
            @Value("${spring.ai.vectorstore.elasticsearch.dimensions}") int dimensions,
            @Value("${spring.ai.vectorstore.elasticsearch.index-name:ai-hackathon-ticket}") String indexName,
            EmbeddingModel embeddingModel, RestClient restClient) {
        var options = new ElasticsearchVectorStoreOptions();
        options.setIndexName(indexName);
        options.setDimensions(dimensions);
        return new ElasticsearchVectorStore(options, restClient, embeddingModel, true);
    }

    @Bean
    public EmbeddingModel embeddingModel(
            @Value("${spring.ai.openai.api-key}") String apiKey,
            @Value("${spring.ai.vectorstore.elasticsearch.dimensions}") int dimensions,
            @Value("${spring.ai.openai.embedding.options.model}") String embeddingModel
    ) {
        // https://docs.spring.io/spring-ai/reference/api/embeddings/openai-embeddings.html
        var options = new OpenAiEmbeddingOptions.Builder()
                .withDimensions(dimensions)
                .withModel(embeddingModel)
                .build();

        return new OpenAiEmbeddingModel(new OpenAiApi(apiKey), MetadataMode.ALL, options);
    }
}
