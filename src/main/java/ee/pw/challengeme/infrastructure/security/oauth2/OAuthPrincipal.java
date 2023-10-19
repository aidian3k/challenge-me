package ee.pw.challengeme.infrastructure.security.oauth2;

import ee.pw.challengeme.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.nio.file.attribute.UserPrincipal;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
@Setter
public class OAuthPrincipal implements OAuth2User, UserPrincipal {

	private Long id;
	private String email;
	private String userName;
	private Map<String, Object> attributes;

	public OAuthPrincipal(Long id, String email, String userName) {
		this.id = id;
		this.email = email;
		this.userName = userName;
	}

	public static OAuthPrincipal of(User user) {
		return new OAuthPrincipal(
			user.getId(),
			user.getEmail(),
			user.getUsername()
		);
	}

	public static OAuthPrincipal of(User user, Map<String, Object> attributes) {
		OAuthPrincipal userPrincipal = OAuthPrincipal.of(user);
		userPrincipal.setAttributes(attributes);

		return userPrincipal;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
	public String getName() {
		return email;
	}
}
