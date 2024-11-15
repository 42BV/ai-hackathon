package nl._42.springai.hackathon.domain.ticket;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findAllByCompletedTrueAndIdIn(Set<Long> ids);

}
