package pl.lunasoftware.demo.security.raceconditionattackdemo;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/teachers")
public class TeacherRankingController {

    private final TeacherRankingService teacherRankingService;

    public TeacherRankingController(TeacherRankingService teacherRankingService) {
        this.teacherRankingService = teacherRankingService;
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<TeacherSummaryDto> getTeachersInfo() {
        return teacherRankingService.getAllTeacherRankings();
    }

    @PostMapping
    public void addTeacherRanking(@Valid @RequestBody TeacherRankingRequest purchaseDto) {
        teacherRankingService.addTeacherRanking(purchaseDto);
    }
}
