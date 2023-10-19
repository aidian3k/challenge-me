package ee.pw.challengeme.infrastructure.clock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.LocalDateTime;

@Configuration
@Slf4j
public class ClockConfiguration {

	@Bean
	public Clock configureClock() {
		Clock utcClock = Clock.systemUTC();
		log.debug("Current time set on clock is: {}", LocalDateTime.now(utcClock));

		return utcClock;
	}
}
