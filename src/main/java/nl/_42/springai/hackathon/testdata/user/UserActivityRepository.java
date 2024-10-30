package nl._42.springai.hackathon.testdata.user;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

    List<UserActivity> findAllByUserIdIn(Set<Long> userIds);

}
