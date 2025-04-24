package pl.lunasoftware.demo.security.raceconditionattackdemo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, UUID> {
    Optional<Ranking> findOneByReviewerIdAndTeacherId(UUID reviewerId, UUID teacherId);

}
