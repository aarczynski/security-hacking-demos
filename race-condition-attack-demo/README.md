# Race Condition Vulnerability
## Running locally
```shell
make start
```
or
```shell
./gradlew clean build && docker-compose up --build
```

## Application description
Vulnerable demo application allows to rate teachers from 1 to 5. User is allowed to rate each teacher only once.
Application checks if a given review exists before submitting a new one:
```java
rankingRepository.findOneByReviewerIdAndTeacherId(teacherRankingRequest.reviewerId(), teacherRankingRequest.teacherId())
        .ifPresent(t -> {
            throw new TeacherRankingExistsException(teacherRankingRequest.reviewerId(), teacherRankingRequest.teacherId());
        });
```

## Vulnerability description
If requests are sent quickly enough, more than one rating from the same user for a given teacher will be saved due to race condition.
![image](./readme-assets/race-condition-parallel.png)
source: https://portswigger.net/research/the-single-packet-attack-making-remote-race-conditions-local

HTTP/2 allows grouping multiple requests and sending them together by a trigger request to minimalize jitter effect. This makes race condition attack even more effective.

![image](./readme-assets/race-condition-single-packet-attack.png)
source: https://portswigger.net/research/the-single-packet-attack-making-remote-race-conditions-local

## Vulnerability expolitation
Send multiple requests quickly to add a review for a particular teacher. You can use Burp Suite or other tool. Example:
```http request
POST /api/v1/teachers HTTP/1.1
Host: localhost:8081
Content-Length: 181
sec-ch-ua-platform: "macOS"
Accept-Language: en-GB,en;q=0.9
sec-ch-ua: "Chromium";v="135", "Not-A.Brand";v="8"
Content-Type: application/json
sec-ch-ua-mobile: ?0
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36
Accept: */*
Origin: http://localhost:8081
Sec-Fetch-Site: same-origin
Sec-Fetch-Mode: cors
Sec-Fetch-Dest: empty
Referer: http://localhost:8081/
Accept-Encoding: gzip, deflate, br
Connection: keep-alive

{"reviewerId":"c884000b-2333-4960-8be1-8bd9c11f1da9","teacherId":"1b423302-3371-477c-87e7-19c25beb4bd6","score":1,"comment":"Terrbile experience!","date":"2025-04-29T13:34:22.533Z"}
```

## Possible fixes
* Adding `UNIQUE (REVIEWER_ID, TEACHER_ID)` constrain in data model,
* Synchronizing `addTeacherRanking(...)` method per user,
* Using `@Transactional(isolation = Isolation.SERIALIZABLE)` on top of the `addTeacherRanking(...)` method. This will cause performance hit.
