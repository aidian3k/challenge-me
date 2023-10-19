package ee.pw.challengeme.infrastructure.security.oauth2.users.types;

import java.util.Map;

public class GitHubUserInfo extends OAuthUserInfo {

	public GitHubUserInfo(Map<String, Object> attributes) {
		super(attributes);
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getEmail() {
		return null;
	}
}
