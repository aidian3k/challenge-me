package ee.pw.challengeme.infrastructure.amazon;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "amazon.s3")
@Component
@Getter
class AmazonS3CredentialsProvider {

	private String accessKey;
	private String secretKey;
}
