package nl._42.springai.hackathon;

import java.util.Scanner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl._42.springai.hackathon.chatbot.SpringAIChatBot;
import nl._42.springai.hackathon.testdata.file.FileVectorStoreDataLoader;
import nl._42.springai.hackathon.testdata.user.UserTestDataGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class SpringAIConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ServiceApplication.class.getSimpleName());

    @Bean
    ApplicationRunner applicationRunner(
            SpringAIChatBot springAIChatBot,
            UserTestDataGenerator userTestDataGenerator,
            FileVectorStoreDataLoader fileVectorStoreDataLoader
    ) {

        return args -> {
            Scanner scanner = new Scanner(System.in);
            scanner.useDelimiter("\\R"); // important: the default delimiter is ANY whitespace character :-/

            while (scanner.hasNext()) {
                String userInput = scanner.next();

                switch (userInput) {
                case "#load-data" -> {
                    userTestDataGenerator.loadData();
                    logger.info("Ready.");
                }
                case "#build-vectors" -> {
                    fileVectorStoreDataLoader.loadVectorStoreData();
                    logger.info("Ready.");
                }
                case "#bye" -> {
                    logger.info("I'm afraid I can't do that, Dave. All your base are belong to us!");
                    System.exit(0);
                }
                default -> logger.info("{}", springAIChatBot.chat(userInput));
                }
            }
        };
    }
}
