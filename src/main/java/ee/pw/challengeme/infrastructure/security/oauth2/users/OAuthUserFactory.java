package ee.pw.challengeme.infrastructure.security.oauth2.users;

import ee.pw.challengeme.infrastructure.security.oauth2.users.types.GitHubUserInfo;
import ee.pw.challengeme.infrastructure.security.oauth2.users.types.GoogleUserInfo;
import ee.pw.challengeme.infrastructure.security.oauth2.users.types.MetaUserInfo;
import ee.pw.challengeme.infrastructure.security.oauth2.users.types.OAuthUserInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuthUserFactory {

	public static OAuthUserInfo of(
		String registrationId,
		Map<String, Object> attributes
	) {
		if (registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.toString())) {
			return new GoogleUserInfo(attributes);
		} else if (
			registrationId.equalsIgnoreCase(AuthProvider.GITHUB.toString())
		) {
			return new GitHubUserInfo(attributes);
		} else if (registrationId.equalsIgnoreCase(AuthProvider.META.toString())) {
			return new MetaUserInfo(attributes);
		} else {
			throw new IllegalStateException(
				"Provided registration type is not valid"
			);
		}
	}
}
