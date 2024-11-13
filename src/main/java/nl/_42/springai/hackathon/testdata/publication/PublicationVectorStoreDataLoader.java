package nl._42.springai.hackathon.testdata.publication;

import static nl._42.springai.hackathon.testdata.BatchUtils.runTasksMultithreaded;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PublicationVectorStoreDataLoader {

    private static final int BATCH_SIZE = 10;

    private final VectorStore vectorStore;
    private final PublicationRepository publicationRepository;

    private static String mapI18NString(Map<String, String> map) {
        return String.join(" : ", map.get("nl"), map.get("en"));
    }

    public void loadVectorStoreData() throws Exception {
        log.info("STARTED");

        var publications = publicationRepository.findAll(PageRequest.of(0, BATCH_SIZE));
        var tasks = new ArrayList<Callable<Void>>();

        IntStream.range(0, publications.getTotalPages())
                .mapToObj(PublicationVectorBuildTask::new)
                .forEach(tasks::add);

        runTasksMultithreaded(tasks);
        log.info("DONE");
    }

    @AllArgsConstructor
    class PublicationVectorBuildTask implements Callable<Void> {

        public final int taskId;

        @Override
        @Transactional
        public Void call() {
            try {
                log.info("Started VectorBuildTask id: {}", taskId);
                var publications = publicationRepository.findAll(PageRequest.of(taskId, BATCH_SIZE));
                var mappedPublications = publications
                        .map(pub -> {
                                    var content = String.join(": ", mapI18NString(pub.getTitle()), mapI18NString(pub.getContent()));
                                    var metadata = new HashMap<String, Object>();
                                    metadata.put("id", pub.getId());
                                    metadata.put("includedTags", String.join(",", pub.getIncludedTags()));
                                    metadata.put("excludedTags", String.join(",", pub.getExcludedTags()));
                                    metadata.put("type", pub.getType().toString());

                                    return new Document(content, metadata);
                                }
                        )
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
