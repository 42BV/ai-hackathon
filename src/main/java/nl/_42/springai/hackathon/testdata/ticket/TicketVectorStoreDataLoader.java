package nl._42.springai.hackathon.testdata.ticket;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl._42.springai.hackathon.testdata.BatchUtils;

import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public
class TicketVectorStoreDataLoader {

    private static final int BATCH_SIZE = 10;

    private final TicketRepository repository;

    private final VectorStore vectorStore;

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