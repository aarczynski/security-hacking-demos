package pl.lunasoftware.demo.security.raceconditionattackdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
@EnableJpaAuditing
public class RaceConditionAttackDemoApplication {

    private static final int ONE_YEAR = 31536000;

    public static void main(String[] args) {
        SpringApplication.run(RaceConditionAttackDemoApplication.class, args);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .requiresChannel(channel -> channel
                        .anyRequest().requiresSecure()
                )
                .headers(headers -> headers
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(ONE_YEAR)
                                .preload(true)
                        )
                );

        return http.build();
    }
}
