package pl.lunasoftware.demo.security.raceconditionattackdemo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherRankingService {
    private final TeacherRepository teacherRepository;
    private final RankingRepository rankingRepository;
    private final UserRepository userRepository;

    public TeacherRankingService(TeacherRepository teacherRepository, RankingRepository rankingRepository, UserRepository userRepository) {
        this.teacherRepository = teacherRepository;
        this.rankingRepository = rankingRepository;
        this.userRepository = userRepository;
    }

    public List<TeacherSummaryDto> getAllTeacherRankings() {
        return teacherRepository.findAllBy().stream()
                .map(t -> new TeacherSummaryDto(
                        t.getId(), t.getName(), t.getAverageRanking(),
                        t.getRankings().stream()
                                .map(r -> new TeacherSummaryDto.RankingDto(r.getReviewer().getName(), r.getScore(), r.getComment(), r.getDateAdded()))
                                .toList()
                ))
                .toList();
    }

    public void addTeacherRanking(TeacherRankingRequest teacherRankingRequest) {
        rankingRepository.findOneByReviewerIdAndTeacherId(teacherRankingRequest.reviewerId(), teacherRankingRequest.teacherId())
                .ifPresent(t -> {
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
    }
}
