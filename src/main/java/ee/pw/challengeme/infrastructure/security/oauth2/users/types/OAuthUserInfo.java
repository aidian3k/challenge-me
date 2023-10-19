package ee.pw.challengeme.infrastructure.security.oauth2.users.types;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class OAuthUserInfo {

	protected Map<String, Object> attributes;

	public abstract String getId();

	public abstract String getName();

	public abstract String getEmail();
}
