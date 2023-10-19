package ee.pw.challengeme.infrastructure.security.oauth2;

import ee.pw.challengeme.infrastructure.util.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final CookieService cookieService;

	@Value("${app.oauth2.redirect-url}")
	private String oAuthRedirectUrl;

	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication
	) throws IOException {
		handle(request, response, authentication);
		super.clearAuthenticationAttributes(request);
	}

	@Override
	protected void handle(
		HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication
	) throws IOException {
		cookieService.createUserTokens(
			(UserDetails) authentication.getPrincipal(),
			response
		);

		response.setStatus(HttpStatus.OK.value());
		response.sendRedirect(oAuthRedirectUrl);
	}
}
