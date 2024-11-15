package nl._42.springai.hackathon.domain.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import groovy.lang.IntRange;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.instancio.Instancio;
import org.instancio.Select;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.javafaker.Faker;

@RequiredArgsConstructor
@Component
@Slf4j
public class UserTestDataGenerator {

    private static final Faker FAKER = Faker.instance();
    private final UserRepository userRepository;
    private final UserReviewRepository userReviewRepository;
    private List<String> mockReviews;

    @PostConstruct
    void setup() {
        mockReviews = loadReviews("/reviews.txt");
    }

    public void loadUserData() throws InterruptedException {
        log.info("Loading test data for users and user activity!");
        var executor = Executors.newCachedThreadPool();
        var tasks = new IntRange(0, 20).stream().map(CreateDataTask::new).toList();
        var futures = executor.invokeAll(tasks);
        executor.shutdown();

        var tasksCompleted = executor.awaitTermination(1, TimeUnit.MINUTES);

        if (!tasksCompleted) {
            throw new IllegalStateException("Terminated before user data loading was finished!");
        }

        for (Future<List<User>> future : futures) {
            try {
                future.get(); // This will throw an exception if the task threw one
            } catch (ExecutionException e) {
                log.error("Task failed with exception: ", e);
            }
        }
    }

    public List<User> createUsers(int size) {
        var users = Instancio.ofList(User.class)
                .size(size)
                .supply(Select.field(User::getId), () -> null)
                .supply(Select.field(User::getName), () -> FAKER.name().name())
                .supply(Select.field(User::getAddress), () -> String.join(" ",
                        List.of(FAKER.address().city(), FAKER.address().streetAddress(), FAKER.address().zipCode(), FAKER.address().country())))
                .supply(Select.field(User::getDateOfBirth), () -> FAKER.date().birthday(20, 100).toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .supply(Select.field(User::getAge), () -> (long) FAKER.number().numberBetween(1, 100))
                .create();
        return userRepository.saveAll(users);
    }

    public List<UserReview> createReviewsForUsers(List<Long> userIds, int size) {
        var reviews = Instancio.ofList(UserReview.class)
                .size(size)
                .supply(Select.field(UserReview::getUserId), random -> userIds.get(random.intRange(0, userIds.size() - 1)))
                .supply(Select.field(UserReview::getReview), random -> mockReviews.get(random.intRange(0, mockReviews.size() - 1)))
                .ignore(Select.field(UserReview::getId))
                .ignore(Select.field(UserReview::getScore))
                .ignore(Select.field(UserReview::getSubject))
                .create();

        return userReviewRepository.saveAll(reviews);
    }

    public List<String> loadReviews(String resourceName) {
        List<String> reviews = new ArrayList<>();

        // Use the class loader to get the resource as an InputStream
        InputStream inputStream = this.getClass().getResourceAsStream(resourceName);

        // Read the InputStream line by line
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                reviews.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return reviews;
    }

    @AllArgsConstructor
    class CreateDataTask implements Callable<List<User>> {

        public final int taskId;

        @Override
        @Transactional
        public List<User> call() {
            log.info("Started CreateDataTask id: {}", taskId);
            var users = createUsers(50);
            createReviewsForUsers(users.stream().map(User::getId).toList(), 50);
            log.info("Finished CreateDataTask id: {}", taskId);
            return users;
        }
    }

}
