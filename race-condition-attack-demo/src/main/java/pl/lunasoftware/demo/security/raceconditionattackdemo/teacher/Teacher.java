package pl.lunasoftware.demo.security.raceconditionattackdemo.teacher;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import pl.lunasoftware.demo.security.raceconditionattackdemo.teacher.ranking.Ranking;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.UUID;

@Entity
@Table(name = "TEACHERS")
public class Teacher {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;

    @OneToMany(mappedBy = "teacher")
    private List<Ranking> rankings = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ranking> getRankings() {
        return rankings;
    }

    public OptionalDouble getAverageRanking() {
        return rankings.stream()
                .mapToInt(Ranking::getScore)
                .average();
    }
}
