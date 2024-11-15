package nl._42.springai.hackathon.domain.ticket;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.IntStream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

@Slf4j
@Component
@RequiredArgsConstructor
public class TicketDataLoader {

    private static final Faker FAKER = Faker.instance();
    private final TicketRepository repository;

    public void generateTicketData() {
        repository.saveAll(IntStream.range(0, 100)
                .parallel()
                .mapToObj(index -> new Ticket(null, FAKER.number().randomNumber(), FAKER.book().title(),
                        Set.of(new Comment(FAKER.number().randomNumber(), FAKER.backToTheFuture().quote())),
                        LocalDateTime.now(), FAKER.book().title(), true)
                ).toList());
    }
}
