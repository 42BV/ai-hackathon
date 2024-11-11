package nl._42.springai.hackathon.testdata.publication;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "publication")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Publication implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> title = new HashMap<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> content = new HashMap<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Set<String> excludedTags = new HashSet<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Set<String> includedTags = new HashSet<>();

    private PublicationType type;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String keywords;

    public static Publication fromPublicationRequest(PublicationCreateRequest request) {
        var publication = new Publication();
        publication.setTitle(Map.of("en", request.titleEn(), "nl", request.titleNl()));
        publication.setContent(Map.of("en", request.contentEn(), "nl", request.contentNl()));
        publication.setIncludedTags(request.includedTags());
        publication.setExcludedTags(request.excludedTags());
        publication.setType(PublicationType.valueOf(request.type()));
        publication.setStartDate(request.startDate());
        publication.setEndDate(request.endDate());
        return publication;
    }

    @Override
    public boolean isNew() {
        return false;
    }

    @Override
    public Long getId() {
        return null;
    }

    enum PublicationType {
        VACANCY,
        NEWS,
        EVENT
    }

}
