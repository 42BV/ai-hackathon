package nl._42.springai.hackathon.chatbot.examples.functioncall.function.sqlquery;

import java.util.List;
import java.util.function.Function;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
public class GetUserSQLAIService implements Function<UserSqlRequest, List<Object>> {

    private final EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<Object> apply(UserSqlRequest userSqlRequest) {
        //Do not try this at home :)
        log.info("AI called with {}", userSqlRequest.query());
        return entityManager.createNativeQuery(userSqlRequest.query()).getResultList();
    }

}
