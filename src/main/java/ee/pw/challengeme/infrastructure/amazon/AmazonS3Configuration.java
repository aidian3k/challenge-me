package ee.pw.challengeme.infrastructure.amazon;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.Socket;

@Configuration
@RequiredArgsConstructor
class AmazonS3Configuration {

	private final AmazonS3CredentialsProvider amazonS3CredentialsProvider;

	@Bean
	@Profile("!local")
	public AmazonS3 productionAmazonConfig() {
		AWSCredentials credentialsProvider = new BasicAWSCredentials(
			amazonS3CredentialsProvider.getAccessKey(),
			amazonS3CredentialsProvider.getSecretKey()
		);

		return AmazonS3ClientBuilder
			.standard()
			.withRegion(Regions.getCurrentRegion().getName())
			.withCredentials(new AWSStaticCredentialsProvider(credentialsProvider))
			.build();
	}

	@Bean
	@Profile({ "local, dev" })
	public AmazonS3 amazonS3LocalService() {
		checkForAmazonMockRunning();
		String currentRegionName = Regions.getCurrentRegion().getName();

		return AmazonS3ClientBuilder
			.standard()
			.withRegion(currentRegionName)
			.withCredentials(
				new AWSCredentialsProvider() {
					@Override
					public AWSCredentials getCredentials() {
						return new BasicAWSCredentials("root", "root");
					}

					@Override
					public void refresh() {
						// empty
					}
				}
			)
			.withEndpointConfiguration(
				new AwsClientBuilder.EndpointConfiguration(
					"localhost:9090",
					currentRegionName
				)
			)
			.build();
	}

	void checkForAmazonMockRunning() {
		int awsMockPort = 9090;
		String host = "localhost";

		try (var socket = new Socket(host, awsMockPort)) {
			// block to check running port
		} catch (Exception exception) {
			throw new UnsupportedOperationException("Mock must be running when");
		}
	}
}
