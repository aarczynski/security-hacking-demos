package pl.lunasoftware.demo.security.raceconditionattackdemo.teacher.ranking;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.lunasoftware.demo.security.raceconditionattackdemo.teacher.Teacher;
import pl.lunasoftware.demo.security.raceconditionattackdemo.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "RANKINGS")
@EntityListeners(AuditingEntityListener.class)
public class Ranking {

    @Id
    @GeneratedValue
    private UUID id;
    private byte score;
    private String comment;
    @CreatedDate
    private LocalDateTime dateAdded;
    @ManyToOne(fetch = FetchType.LAZY)
    private Teacher teacher;
    @ManyToOne(fetch = FetchType.LAZY)
    private User reviewer;

    public Ranking() {
    }

    public Ranking(User reviewer, byte score, String comment, Teacher teacher) {
        this.score = score;
        this.comment = comment;
        this.teacher = teacher;
        this.reviewer = reviewer;
    }

    public byte getScore() {
        return score;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public User getReviewer() {
        return reviewer;
    }
}
