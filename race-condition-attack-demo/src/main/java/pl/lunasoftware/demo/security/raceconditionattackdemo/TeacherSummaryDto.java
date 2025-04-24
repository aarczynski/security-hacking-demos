package pl.lunasoftware.demo.security.raceconditionattackdemo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.OptionalDouble;
import java.util.UUID;

public record TeacherSummaryDto(
        UUID id,
        String name,
        OptionalDouble averageRanking,
        List<RankingDto> rankings
) {

    record RankingDto(
            String reviewer,
            byte score,
            String comment,
            LocalDateTime dateAdded
    ) {

    }
}
