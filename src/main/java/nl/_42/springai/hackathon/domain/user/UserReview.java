package nl._42.springai.hackathon.domain.user;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.domain.Persistable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserReview implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String review;

    private LocalDateTime timestamp;

    /**
     * Score should be between 0 - 10
     * 1 = bad
     * 5 = neutral
     * 10 = amazing
     * <p>
     * We will let AI decide this
     */
    private Long score;

    /**
     * We will let AI decide this
     */
    private String subject;

    @Override
    public boolean isNew() {
        return this.id != null;
    }
}
