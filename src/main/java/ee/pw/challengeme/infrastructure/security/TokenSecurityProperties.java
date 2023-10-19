package ee.pw.challengeme.infrastructure.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app.token.constants")
@Component
@Getter
@Setter
class TokenSecurityProperties {

	private int shortTokenExpirationLength;
	private int refreshTokenExpirationLength;
	private String tokenSecretKey;
}
