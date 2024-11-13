package nl._42.springai.hackathon.testdata.ticket;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.ai.document.Document;
import org.springframework.data.domain.Persistable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ticket implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String title;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Set<Comment> comments = new HashSet<>();

    private LocalDateTime createdAt;

    private String description;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public boolean isNew() {
        return this.id == null;
    }

    public static Ticket fromTicketRequest(TicketCreateRequest request) {
        var ticket = new Ticket();
        ticket.setTitle(request.title());
        ticket.setDescription(request.description());
        ticket.setCreatedAt(request.createdAt());
        ticket.setComments(request.comments());
        return ticket;
    }

    public Document toDocument() {
        var content = String.join(":", this.title, this.description);
        var metadata = new HashMap<String, Object>();
        metadata.put("userId", this.userId.toString());
        metadata.put("createdAt", this.createdAt.toString());
        metadata.put("title", this.title);
        metadata.put("comments", this.comments.stream().map(Comment::toString).reduce((a, b) -> a + "," + b).orElse(null));
        return new Document(content, metadata);
    }
}
