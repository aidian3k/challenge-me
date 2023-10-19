package ee.pw.challengeme.application;

import ee.pw.challengeme.domain.user.data.UserAuthenticationFacade;
import ee.pw.challengeme.domain.user.data.UserFacade;
import ee.pw.challengeme.domain.user.dto.UserLoginRequest;
import ee.pw.challengeme.domain.user.dto.UserLoginResponse;
import ee.pw.challengeme.domain.user.dto.UserRegistrationRequest;
import ee.pw.challengeme.domain.user.dto.UserRegistrationResponse;
import io.vavr.control.Try;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
class AuthenticationController {

	private final UserFacade userFacade;
	private final UserAuthenticationFacade userAuthenticationFacade;

	@PostMapping("/create-user")
	public ResponseEntity<UserRegistrationResponse> createUser(
		@RequestBody @Valid UserRegistrationRequest userRegistrationDTO,
		HttpServletResponse httpServletResponse
	) {
		return new ResponseEntity<>(
			userFacade.createUser(userRegistrationDTO, httpServletResponse),
			HttpStatus.CREATED
		);
	}

	@PostMapping("/login")
	public ResponseEntity<UserLoginResponse> authenticateUser(
		@RequestBody UserLoginRequest userLoginRequestDTO,
		HttpServletResponse httpServletResponse
	) {
		UserLoginResponse userLoginResponse = userAuthenticationFacade.authenticateUserCredentials(
			userLoginRequestDTO,
			httpServletResponse
		);

		return new ResponseEntity<>(userLoginResponse, HttpStatus.OK);
	}

	@GetMapping("/refresh-token")
	public ResponseEntity<Void> handleRefreshToken(
		HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse
	) {
		Try<Void> refreshTry = userAuthenticationFacade.refreshToken(
			httpServletRequest,
			httpServletResponse
		);

		if (refreshTry.isSuccess()) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}
}
