package pl.lunasoftware.demo.security.raceconditionattackdemo.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import pl.lunasoftware.demo.security.raceconditionattackdemo.teacher.ranking.Ranking;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;

    @OneToMany(mappedBy = "reviewer")
    private List<Ranking> reviews = new ArrayList<>();

    public String getName() {
        return name;
    }
}
