package nl._42.springai.hackathon.testdata.file;

import static nl._42.springai.hackathon.testdata.BatchUtils.runTasksMultithreaded;
import static nl._42.springai.hackathon.testdata.BatchUtils.splitListIntoBatches;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileVectorStoreDataLoader {

    private static final int BATCH_SIZE = 10;
    public static final TokenTextSplitter TEXT_SPLITTER = new TokenTextSplitter();
    private static final String FILE_NAME = "/file";

    private final VectorStore vectorStore;
    private final RestClient elasticsearchClient;

    public void loadVectorStoreData() throws Exception {
        log.info("STARTED");
        TextReader reader = new TextReader(FILE_NAME);

        var documents = reader.read();

        var splitDocuments = TEXT_SPLITTER.apply(documents);
        log.info("Split into {} documents", splitDocuments.size());

        var batches = splitListIntoBatches(splitDocuments, BATCH_SIZE);
        log.info("Processed into {} batches", batches.size());

        List<Callable<Void>> tasks = new ArrayList<>();
        for (List<Document> batch : batches) {
            tasks.add(new FileVectorBuildTask(batch));
        }

        runTasksMultithreaded(tasks);
        log.info("DONE in {} batches", batches.size());
    }

    public void cleanVectors(String indexName) {
        try {
            // this will drop the existing data from the vector store (start with a clean slate)
            log.info("deleting: {}", indexName);
            elasticsearchClient.performRequest(new Request(HttpMethod.DELETE.name(), "http://localhost:9200/" + indexName));
        } catch (Exception ex) {
            log.info("Index {} not found", indexName);
        }
    }

    @AllArgsConstructor
    class FileVectorBuildTask implements Callable<Void> {

        public final List<Document> documents;

        @Override
        @Transactional
        public Void call() {
            try {
                log.info("Started VectorBuildTask");
                vectorStore.accept(documents);
                log.info("Finished VectorBuildTask id");
            } catch (Exception e) {
                log.error("Failed task", e);
            }
            return null;
        }
    }

}
