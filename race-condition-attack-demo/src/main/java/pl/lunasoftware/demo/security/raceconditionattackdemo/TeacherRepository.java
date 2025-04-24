package pl.lunasoftware.demo.security.raceconditionattackdemo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, UUID> {
    @Query("SELECT t FROM Teacher t LEFT JOIN FETCH t.rankings r JOIN FETCH r.reviewer")
    List<Teacher> findAllBy();
}
