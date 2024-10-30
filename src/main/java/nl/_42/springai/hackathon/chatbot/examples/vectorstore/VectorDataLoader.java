package nl._42.springai.hackathon.chatbot.examples.vectorstore;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl._42.springai.hackathon.testdata.user.User;
import nl._42.springai.hackathon.testdata.user.UserActivityRepository;
import nl._42.springai.hackathon.testdata.user.UserRepository;
import nl._42.springai.hackathon.testdata.user.VectorStoreUser;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Slf4j
@RequiredArgsConstructor
public class VectorDataLoader {

    // static final String dataFileName = "/burgerlijk-wetboek-4.txt";
    // static final String dataFileName = "/burgerlijk-wetboek-8.txt";
    // static final String dataFileName = "/voetbal.txt";
    // static final String dataFileName = "/wegenverkeerswet-1994.txt";
    //    static final String dataFileName = "/world_80_days.txt";

    //    if (null == ServiceApplication .class.getResource(dataFileName)) {
    //        throw new FileNotFoundException("The datafile resource could not be found: " + dataFileName);
    //    }

    private static final int BATCH_SIZE = 10;
    private static final int MAX_CONCURRENT_TASKS = 25;
    private final UserRepository userRepository;
    private final UserActivityRepository userActivityRepository;
    private final ObjectMapper objectMapper;
    private final VectorStore vectorStore;

    public void cleanVectors(RestClient client, String indexName) throws IOException {
        // this will drop the existing data from the vector store (start with a clean slate)
        log.info("deleting: {}", indexName);
        client.performRequest(new Request(HttpMethod.DELETE.name(), "http://localhost:9200/" + indexName));
    }

    public void buildVectors() throws Exception {
        int userPageCount = (int) Math.ceil((double) userRepository.count() / BATCH_SIZE);

        //Multi-threaded processing up to a max of MAX_CONCURRENT_TASKS at the same time
        ExecutorService executor = Executors.newFixedThreadPool(MAX_CONCURRENT_TASKS);
        Semaphore semaphore = new Semaphore(MAX_CONCURRENT_TASKS);
        List<Future<Void>> futures = IntStream.range(0, userPageCount)
                .mapToObj(i -> executor.submit(() -> {
                    try {
                        semaphore.acquire();
                        return new VectorBuildTask(i).call();
                    } finally {
                        semaphore.release();
                    }
                }))
                .toList();

        executor.shutdown();
        boolean tasksCompleted = executor.awaitTermination(30, TimeUnit.MINUTES);

        if (!tasksCompleted) {
            throw new IllegalStateException("Terminated before user data loading was finished!");
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

        public final int taskId; //Also serves as page nr

        @Override
        @Transactional
        public Void call() {
            try {
                log.info("Started VectorBuildTask id: {}", taskId);
                var textSplitter = new TokenTextSplitter();
                var users = userRepository.findAll(PageRequest.of(taskId, BATCH_SIZE));
                var userIds = users.map(User::getId).toSet();
                var userActivities = userActivityRepository.findAllByUserIdIn(userIds);

                var mapped = users.getContent().stream().map(user -> {
                    var activities = userActivities.stream().filter(a -> a.getUserId().equals(user.getId())).toList();
                    return VectorStoreUser.fromUser(user, activities);
                }).toList();

                var reader = new JsonReader(new ByteArrayResource(objectMapper.writeValueAsBytes(mapped)));

                vectorStore.accept(textSplitter.apply(reader.read()));
                log.info("Finished VectorBuildTask id: {}", taskId);
            } catch (Exception e) {
                log.error("Failed task id: {}", taskId, e);
            }
            return null;
        }
    }

}
