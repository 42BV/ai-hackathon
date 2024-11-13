package nl._42.springai.hackathon.testdata.ticket;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl._42.springai.hackathon.testdata.BatchUtils;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.javafaker.Faker;

@Slf4j
@Component
@RequiredArgsConstructor
public class TicketDataLoader {

    private static final int BATCH_SIZE = 10;
    private static final Faker FAKER = Faker.instance();
    private final TicketRepository repository;
    private final VectorStore vectorStore;

    public void generateTicketData() {
        repository.saveAll(IntStream.range(0, 100)
                .parallel()
                .mapToObj(index -> new Ticket(null, FAKER.number().randomNumber(), FAKER.book().title(), Set.of(new Comment(FAKER.number().randomNumber(), FAKER.backToTheFuture().quote())),
                        LocalDateTime.now(), FAKER.book().title())
                ).toList());

        try {
            new TicketVectorStoreDataLoader().loadVectorStoreData();
        } catch (Exception e) {
            log.error("Failed to load vector store data", e);
        }
    }

    class TicketVectorStoreDataLoader {
        public void loadVectorStoreData() throws Exception {
            log.info("STARTED");

            var tickets = repository.findAll(PageRequest.of(0, BATCH_SIZE));
            var tasks = new ArrayList<Callable<Void>>();

            IntStream.range(0, tickets.getTotalPages())
                    .mapToObj(TicketVectorBuildTask::new)
                    .forEach(tasks::add);

            BatchUtils.runTasksMultithreaded(tasks);
            log.info("DONE");
        }
    }

    @AllArgsConstructor
    class TicketVectorBuildTask implements Callable<Void> {

        public final int taskId;

        @Override
        @Transactional
        public Void call() {
            try {
                log.info("Started VectorBuildTask id: {}", taskId);
                var publications = repository.findAll(PageRequest.of(taskId, BATCH_SIZE));
                var mappedPublications = publications
                        .map(Ticket::toDocument)
                        .toList();

                vectorStore.accept(mappedPublications);
                log.info("Finished VectorBuildTask id: {}", taskId);
            } catch (Exception e) {
                log.error("Failed task id: {}", taskId, e);
            }
            return null;
        }
    }

}
