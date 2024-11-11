package nl._42.springai.hackathon.testdata.file;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

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
    private static final String FILE_NAME = "/beemovie";
    private static final int MAX_CONCURRENT_TASKS = 25;

    private final VectorStore vectorStore;
    private final RestClient elasticsearchClient;

    public static <T> List<List<T>> splitListIntoBatches(List<T> list, int batchSize) {
        List<List<T>> batches = new ArrayList<>();
        int totalSize = list.size();

        for (int i = 0; i < totalSize; i += batchSize) {
            int end = Math.min(i + batchSize, totalSize);
            batches.add(new ArrayList<>(list.subList(i, end)));
        }

        return batches;
    }

    public void loadVectorStoreData() throws Exception {
        log.info("STARTED");

        TextReader reader = new TextReader(FILE_NAME);

        var documents = reader.read();
        log.info("Processed into {} documents", documents.size());

        //This only works if there are multiple documents(files) :)
        var batches = splitListIntoBatches(documents, BATCH_SIZE);

        List<Callable<Void>> tasks = new ArrayList<>();
        for (List<Document> batch : batches) {
            tasks.add(new VectorBuildTask(batch));
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

    /**
     * This runs provided tasks multithreaded. Useful for building vectors.
     */
    public void runTasksMultithreaded(List<Callable<Void>> tasks) throws Exception {
        // Multithreaded processing up to a max of MAX_CONCURRENT_TASKS at the same time
        ExecutorService executor = Executors.newFixedThreadPool(MAX_CONCURRENT_TASKS);
        Semaphore semaphore = new Semaphore(MAX_CONCURRENT_TASKS);

        var futures = tasks.stream().map(task -> executor.submit(() -> {
            try {
                semaphore.acquire();
                return task.call();
            } finally {
                semaphore.release();
            }
        })).toList();

        executor.shutdown();
        boolean tasksCompleted = executor.awaitTermination(30, TimeUnit.MINUTES);

        if (!tasksCompleted) {
            throw new IllegalStateException("Terminated before tasks were finished!");
        }

        for (Future<Void> future : futures) {
            try {
                future.get(); // This will throw an exception if the task threw one
            } catch (ExecutionException e) {
                log.error("Task failed with exception: ", e);
            }
        }
    }

    @AllArgsConstructor
    class VectorBuildTask implements Callable<Void> {

        public static final TokenTextSplitter TEXT_SPLITTER = new TokenTextSplitter();
        public final List<Document> documents;

        @Override
        @Transactional
        public Void call() {
            try {
                log.info("Started VectorBuildTask");
                vectorStore.accept(TEXT_SPLITTER.apply(documents));
                log.info("Finished VectorBuildTask id");
            } catch (Exception e) {
                log.error("Failed task", e);
            }
            return null;
        }
    }

}
