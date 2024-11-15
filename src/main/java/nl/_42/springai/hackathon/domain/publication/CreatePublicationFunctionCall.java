package nl._42.springai.hackathon.domain.publication;

import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class CreatePublicationFunctionCall implements Function<PublicationCreateRequest, Publication> {

    private final PublicationRepository publicationRepository;

    @Override
    public Publication apply(PublicationCreateRequest request) {
        log.info("AI called with {}", request);
        return publicationRepository.save(Publication.fromPublicationRequest(request));
    }

}
