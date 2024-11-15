package nl._42.springai.hackathon.domain.user;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')) AND u.dateOfBirth = :dateOfBirth")
    List<User> findByNameContainingIgnoreCaseAndDateOfBirth(@Param("name") String name, @Param("dateOfBirth") LocalDate dateOfBirth);

}
