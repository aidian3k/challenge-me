package ee.pw.challengeme.infrastructure.security.oauth2;

import ee.pw.challengeme.domain.user.data.UserManagementService;
import ee.pw.challengeme.domain.user.data.UserRepository;
import ee.pw.challengeme.domain.user.entity.User;
import ee.pw.challengeme.infrastructure.security.oauth2.users.OAuthUserFactory;
import ee.pw.challengeme.infrastructure.security.oauth2.users.types.OAuthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuthUserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;
	private final UserManagementService userManagementService;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest)
		throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		OAuthUserInfo oAuthUserInfo = OAuthUserFactory.of(
			userRequest.getClientRegistration().getRegistrationId(),
			oAuth2User.getAttributes()
		);
		Optional<User> userOptional = userRepository.findUserByEmail(
			oAuthUserInfo.getEmail()
		);
		User user;

		if (userOptional.isPresent()) {
			user = userManagementService.updateUser(userOptional.get());
		} else {
			User createdUser = User
				.builder()
				.userName(oAuth2User.getName())
				.email(oAuthUserInfo.getEmail())
				.build();

			user = userManagementService.saveUser(createdUser);
		}

		OAuthPrincipal oAuthPrincipal = OAuthPrincipal.of(
			user,
			oAuth2User.getAttributes()
		);
		SecurityContextHolder
			.getContext()
			.setAuthentication((Authentication) oAuthPrincipal);

		return oAuthPrincipal;
	}
}
