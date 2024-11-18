package nl._42.springai.hackathon.domain.ticket;

import static java.util.stream.Collectors.joining;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl._42.springai.hackathon.chatbot.lab.create.TicketCreateRequest;

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
    private List<Comment> comments = new ArrayList<>();

    private LocalDateTime createdAt;

    private String description;

    private boolean completed;

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
        ticket.setUserId(request.userId());
        ticket.setTitle(request.title());
        ticket.setDescription(request.description());
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setComments(new ArrayList<>());
        ticket.setCompleted(false);
        return ticket;
    }

    public void addComment(Comment comment) {
        if (comment == null) {
            return;
        }

        addComment(List.of(comment));
    }

    public void addComment(List<Comment> comments) {
        if (comments == null) {
            return;
        }
        this.comments.addAll(comments);
    }

    public Document toDocument() {
        var content = String.join(" : ", this.title, this.description, mapCommentsToContent());
        var metadata = new HashMap<String, Object>();
        metadata.put("userId", this.userId.toString());
        metadata.put("createdAt", this.createdAt.toString());
        metadata.put("title", this.title);
        metadata.put("completed", this.completed);
        metadata.put("comments", this.comments.stream().map(Comment::toString).reduce((a, b) -> a + "," + b).orElse(null));
        return new Document(content, metadata);
    }

    public String mapCommentsToContent() {
        return this.comments.stream().map(Comment::content).collect(joining(" | "));
    }
}
