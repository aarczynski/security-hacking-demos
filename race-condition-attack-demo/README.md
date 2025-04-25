# Race Condition Vulnerability
## Application description
Vulnerable demo application allows to rate teachers from 1 to 5. User is allowed to rate each teacher only once.
Application checks if given revies existis before submitting a new one:
```java
rankingRepository.findOneByReviewerIdAndTeacherId(teacherRankingRequest.reviewerId(), teacherRankingRequest.teacherId())
        .ifPresent(t -> {
            throw new TeacherRankingExistsException(teacherRankingRequest.reviewerId(), teacherRankingRequest.teacherId());
        });
```
If requests are sent quickly enough, more than one rating from the same user for a given teacher will be saved due to race condition.

![image](../readme-assets/race-condition-parallel.png)

source: https://portswigger.net/research/the-single-packet-attack-making-remote-race-conditions-local

HTTP/2 allows grouping multiple requests and sending them together by a trigger request to minimalize jitter effect. This makes race condition attack even more effective.

![image](../readme-assets/race-condition-single-packet-attack.png)

source: https://portswigger.net/research/the-single-packet-attack-making-remote-race-conditions-local

## Possible fixes
* Adding `UNIQUE (REVIEWER_ID, TEACHER_ID)` constrain in data model,
* Using `@Transactional(isolation = Isolation.SERIALIZABLE)` on top of the `addTeacherRanking(...)` method.
