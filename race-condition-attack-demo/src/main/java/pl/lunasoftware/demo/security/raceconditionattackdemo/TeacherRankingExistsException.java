package pl.lunasoftware.demo.security.raceconditionattackdemo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.CONFLICT)
public class TeacherRankingExistsException extends RuntimeException {
    public TeacherRankingExistsException(UUID reviewerId, UUID teacherId) {
        super(String.format("teacher %s already has a review from user %s", teacherId, reviewerId));
    }
}
