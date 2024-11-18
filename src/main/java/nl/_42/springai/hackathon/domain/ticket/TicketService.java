package nl._42.springai.hackathon.domain.ticket;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketService {

    private final VectorStore vectorStore;
    private final TicketRepository repository;

    @Transactional
    public Ticket createTicket(Ticket ticket) {
        return repository.save(ticket);
    }

    @Transactional
    public Ticket addComment(long ticketId, Comment comment) {
        var ticket = repository.findById(ticketId).orElseThrow();
        ticket.addComment(comment);
        return repository.save(ticket);
    }

    @Transactional
    public Ticket markTicketAsCompleted(long ticketId) {
        var ticket = repository.findById(ticketId).orElseThrow();
        ticket.setCompleted(true);
        ticket = repository.save(ticket);
        storeTicketInVectorStore(Set.of(ticketId));
        return ticket;
    }

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
