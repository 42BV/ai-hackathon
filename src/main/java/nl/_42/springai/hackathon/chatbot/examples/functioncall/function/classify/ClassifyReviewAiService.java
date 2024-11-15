package nl._42.springai.hackathon.chatbot.examples.functioncall.function.classify;

import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl._42.springai.hackathon.domain.user.UserReview;
import nl._42.springai.hackathon.domain.user.UserReviewRepository;

import org.springframework.transaction.annotation.Transactional;

/**
 * These kind of data processing jobs should be done async, potentially using the Batch API
 * <a href="https://platform.openai.com/docs/guides/batch">...</a>
 */
@RequiredArgsConstructor
@Slf4j
public class ClassifyReviewAiService implements Function<ClassifyReviewRequest, UserReview> {

    private final UserReviewRepository reviewRepository;

    @Override
    @Transactional
    public UserReview apply(ClassifyReviewRequest classifyReviewRequest) {
        log.info("AI called with {}", classifyReviewRequest);
        var review = reviewRepository.findById(classifyReviewRequest.id()).orElseThrow();
        review.setScore(classifyReviewRequest.score());
        review.setSubject(classifyReviewRequest.subject());
        return reviewRepository.save(review);
    }
}
