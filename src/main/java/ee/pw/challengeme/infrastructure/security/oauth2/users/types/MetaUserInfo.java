package ee.pw.challengeme.infrastructure.security.oauth2.users.types;

import java.util.Map;

public class MetaUserInfo extends OAuthUserInfo {

	public MetaUserInfo(Map<String, Object> attributes) {
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
