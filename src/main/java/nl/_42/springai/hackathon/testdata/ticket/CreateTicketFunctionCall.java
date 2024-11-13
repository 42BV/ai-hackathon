package nl._42.springai.hackathon.testdata.ticket;

import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CreateTicketFunctionCall implements Function<TicketCreateRequest, Ticket> {

    private final TicketRepository ticketRepository;

    @Override
    public Ticket apply(TicketCreateRequest ticketCreateRequest) {
        log.info("AI called with {}", ticketCreateRequest);
        return ticketRepository.save(Ticket.fromTicketRequest(ticketCreateRequest));
    }
}
