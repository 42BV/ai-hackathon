package nl._42.springai.hackathon.testdata.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReviewRepository extends JpaRepository<UserReview, Long> {

}
