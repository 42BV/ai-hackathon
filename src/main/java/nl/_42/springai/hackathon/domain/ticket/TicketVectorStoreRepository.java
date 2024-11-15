package nl._42.springai.hackathon.domain.ticket;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketVectorStoreRepository {

    private final VectorStore vectorStore;
    private final TicketRepository repository;

    public void storeTicketInVectorStore(Set<Long> ticketIds) {
        var tickets = repository.findAllByCompletedTrueAndIdIn(ticketIds);

        if (ticketIds.size() != tickets.size()) {
            var storedIds = tickets.stream().map(Ticket::getId).collect(Collectors.toSet());
            log.info("Skipped tickets due to not being completed. Provided: {}, Stored: {}", ticketIds, storedIds);
        }

        var mappedTickets = tickets
                .stream()
                .map(Ticket::toDocument)
                .toList();

        vectorStore.accept(mappedTickets);
    }
}
