package nl._42.springai.hackathon.chatbot.examples.functioncall.function.getuser;

import java.util.List;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl._42.springai.hackathon.domain.user.User;
import nl._42.springai.hackathon.domain.user.UserRepository;

@RequiredArgsConstructor
@Slf4j
public class GetUserAIService implements Function<UserRequest, List<User>> {

    private final UserRepository userRepository;

    @Override
    public List<User> apply(UserRequest userRequest) {
        log.info("AI called with {} - {}", userRequest.name(), userRequest.dateOfBirth());
        return userRepository.findByNameContainingIgnoreCaseAndDateOfBirth(userRequest.name(), userRequest.dateOfBirth());
    }

}
