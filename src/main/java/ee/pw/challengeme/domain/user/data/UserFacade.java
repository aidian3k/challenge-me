package ee.pw.challengeme.domain.user.data;

import ee.pw.challengeme.domain.user.dto.UserRegistrationRequest;
import ee.pw.challengeme.domain.user.dto.UserRegistrationResponse;
import ee.pw.challengeme.domain.user.entity.User;
import ee.pw.challengeme.domain.user.mapper.UserRegistrationRequestMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserFacade {

	@Delegate
	private final CurrentUserService currentUserService;

	private final UserRegistrationRequestMapper userRegistrationRequestMapper;
	private final UserManagementService userManagementService;

	public UserRegistrationResponse createUser(
		UserRegistrationRequest userRegistrationRequest,
		HttpServletResponse httpServletResponse
	) {
		User mappedUser = User
			.builder()
			.userName(userRegistrationRequest.getUserName())
			.email(userRegistrationRequest.getEmail())
			.name(userRegistrationRequest.getName())
			.password(userRegistrationRequest.getPassword())
			.build();
		return userManagementService.createUser(mappedUser, httpServletResponse);
	}


}
