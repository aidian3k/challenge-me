package ee.pw.challengeme.infrastructure.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@Profile("!local")
@EnableAsync
@EnableScheduling
public class AsyncConfiguration {}
