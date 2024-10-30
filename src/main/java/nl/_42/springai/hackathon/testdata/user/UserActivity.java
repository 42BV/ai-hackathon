package nl._42.springai.hackathon.testdata.user;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class UserActivity implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private UserActivityAction action;

    private LocalDateTime actionTimestamp;

    @Override
    public boolean isNew() {
        return this.id != null;
    }

    enum UserActivityAction {
        BROWSE,
        CLICK_LINK,
        CHECKOUT,
        EXIT
    }

}
