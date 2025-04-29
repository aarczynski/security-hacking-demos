package pl.lunasoftware.demo.security.raceconditionattackdemo.teacher.ranking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.lunasoftware.demo.security.raceconditionattackdemo.teacher.Teacher;
import pl.lunasoftware.demo.security.raceconditionattackdemo.teacher.TeacherRepository;
import pl.lunasoftware.demo.security.raceconditionattackdemo.user.UserRepository;
import pl.lunasoftware.demo.security.raceconditionattackdemo.user.User;

import java.util.List;

@Service
public class TeacherRankingService {
    private static final Logger log = LoggerFactory.getLogger(TeacherRankingService.class);

    private final TeacherRepository teacherRepository;
    private final RankingRepository rankingRepository;
    private final UserRepository userRepository;

    public TeacherRankingService(TeacherRepository teacherRepository, RankingRepository rankingRepository, UserRepository userRepository) {
        this.teacherRepository = teacherRepository;
        this.rankingRepository = rankingRepository;
        this.userRepository = userRepository;
    }

    public List<TeacherSummaryDto> getAllTeacherRankings() {
        List<TeacherSummaryDto> result = teacherRepository.findAllBy().stream()
                .map(t -> new TeacherSummaryDto(
                        t.getId(), t.getName(), t.getAverageRanking(),
                        t.getRankings().stream()
                                .map(r -> new TeacherSummaryDto.RankingDto(r.getReviewer().getName(), r.getScore(), r.getComment(), r.getDateAdded()))
                                .toList()
                ))
                .toList();
        log.info("Teacher rankings found: {}", result);
        return result;
    }

    public void addTeacherRanking(TeacherRankingRequest teacherRankingRequest) {
        rankingRepository.findOneByReviewerIdAndTeacherId(teacherRankingRequest.reviewerId(), teacherRankingRequest.teacherId())
                .ifPresent(t -> {
                    log.info("Rejecting review from user {} of teacher {}. It already exists.", teacherRankingRequest.reviewerId(), teacherRankingRequest.teacherId());
                    throw new TeacherRankingExistsException(teacherRankingRequest.reviewerId(), teacherRankingRequest.teacherId());
                });

        User reviewer = userRepository.findById(teacherRankingRequest.reviewerId())
                .orElseThrow(() -> new IllegalStateException(String.format("User %s is missing", teacherRankingRequest.reviewerId())));
        Teacher teacher = teacherRepository.findById(teacherRankingRequest.teacherId())
                .orElseThrow(() -> new IllegalStateException(String.format("Teacher %s is missing", teacherRankingRequest.teacherId())));

        Ranking ranking = new Ranking(
                reviewer,
                teacherRankingRequest.score(),
                teacherRankingRequest.comment(),
                teacher
        );

        rankingRepository.save(ranking);
        log.info("Added ranking from {} of teacher {}", teacherRankingRequest.reviewerId(), teacherRankingRequest.teacherId());
    }
}
