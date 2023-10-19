package ee.pw.challengeme.domain.user.data;

import ee.pw.challengeme.domain.user.dto.UserRegistrationResponse;
import ee.pw.challengeme.domain.user.entity.User;
import ee.pw.challengeme.infrastructure.util.CookieService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserManagementService {

	private final UserRepository userRepository;
	private final CookieService cookieService;
	private final PasswordEncoder passwordEncoder;

	public UserRegistrationResponse createUser(
		User creationUser,
		HttpServletResponse httpServletResponse
	) {
		SecurityContextHolder
			.getContext()
			.setAuthentication(
				new UsernamePasswordAuthenticationToken(
					creationUser.getEmail(),
					creationUser.getPassword()
				)
			);

		creationUser.setPassword(
			passwordEncoder.encode(creationUser.getPassword())
		);
		User savedUser = saveUser(creationUser);

		cookieService.createUserTokens(savedUser, httpServletResponse);

		return UserRegistrationResponse
			.builder()
			.id(savedUser.getId())
			.name(savedUser.getName())
			.email(savedUser.getEmail())
			.userName(savedUser.getUsername())
			.build();
	}

	public User saveUser(User saveUser) {
		log.debug("Saving user with id=[{}]", saveUser.getId());
		return userRepository.save(saveUser);
	}

	public User updateUser(User user) {
		log.debug("Updating user with id=[{}]", user.getId());
		return userRepository.save(user);
	}

	public void deleteUser(User user) {
		log.debug("Deleting user with id=[{}]", user.getId());
		userRepository.delete(user);
	}
}
