package pl.lunasoftware.demo.security.raceconditionattackdemo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDateTime;
import java.util.UUID;

public record TeacherRankingRequest(
        @NotNull(message = "reviewerId must not be null")
        UUID reviewerId,
        @NotNull(message = "teacherId must not be null")
        UUID teacherId,
        @Min(value = 1, message = "score must be between 1 and 5")
        @Max(value = 5, message = "score must be between 1 and 5")
        byte score,
        @NotBlank(message = "comment must not be empty")
        String comment,
        @Past(message = "date must be from past")
        LocalDateTime date
) {
    public TeacherRankingRequest(UUID reviewerId, UUID teacherId, byte score, String comment) {
        this(reviewerId, teacherId, score, comment, LocalDateTime.now());
    }
}
