package ee.pw.challengeme.domain.user.data;

import com.sun.jdi.request.InvalidRequestStateException;
import ee.pw.challengeme.domain.user.dto.UserLoginRequest;
import ee.pw.challengeme.domain.user.dto.UserLoginResponse;
import ee.pw.challengeme.domain.user.entity.User;
import ee.pw.challengeme.infrastructure.security.TokenProvider;
import ee.pw.challengeme.infrastructure.util.CookieService;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserAuthenticationFacade {

	private final AuthenticationProvider authenticationProvider;
	private final CookieService cookieService;
	private final UserFinderService userFinderService;
	private final TokenProvider tokenProvider;

	public UserLoginResponse authenticateUserCredentials(
		UserLoginRequest userLoginRequest,
		HttpServletResponse httpServletResponse
	) {
		Either<Throwable, UserLoginResponse> authenticationEither = Try
			.of(() -> {
				authenticationProvider.authenticate(
					new UsernamePasswordAuthenticationToken(
						userLoginRequest.email(),
						userLoginRequest.password()
					)
				);

				User authenticatedUser = userFinderService.getUserByEmail(
					userLoginRequest.email()
				);
				cookieService.createUserTokens(authenticatedUser, httpServletResponse);

				return UserLoginResponse
					.builder()
					.userName(authenticatedUser.getUsername())
					.name(authenticatedUser.getName())
					.id(authenticatedUser.getId())
					.email(authenticatedUser.getEmail())
					.build();
			})
			.onFailure(throwable ->
				log.debug(
					"User with email: [{}] has not been properly authenticated with message: [{}]",
					userLoginRequest.email(),
					throwable.getMessage()
				)
			)
			.toEither();

		if (authenticationEither.isLeft()) {
			throw new InvalidRequestStateException("Invalid request given");
		}

		return authenticationEither.get();
	}

	public Try<Void> refreshToken(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse
	) {
		return Try
			.run(() -> {
				final String accessToken = httpServletRequest.getHeader(
					HttpHeaders.AUTHORIZATION
				);
				final String userEmail = tokenProvider.extractUserSubjectFromToken(
					accessToken
				);
				final User user = userFinderService.getUserByEmail(userEmail);
				cookieService.createUserTokens(user, httpServletResponse);
			})
			.onFailure(
				(
					throwable ->
						log.debug(
							"Failure while trying to refresh token: {}",
							throwable.getMessage()
						)
				)
			);
	}
}
