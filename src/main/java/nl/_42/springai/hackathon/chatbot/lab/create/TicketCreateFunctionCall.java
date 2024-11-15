package nl._42.springai.hackathon.chatbot.lab.create;

import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl._42.springai.hackathon.domain.ticket.Ticket;
import nl._42.springai.hackathon.domain.ticket.TicketRepository;

@Slf4j
@RequiredArgsConstructor
public class TicketCreateFunctionCall implements Function<TicketCreateRequest, Ticket> {

    private final TicketRepository ticketRepository;

    @Override
    public Ticket apply(TicketCreateRequest ticketCreateRequest) {
        log.info("AI called with {}", ticketCreateRequest);
        return ticketRepository.save(Ticket.fromTicketRequest(ticketCreateRequest));
    }
}
