package ee.pw.challengeme.infrastructure.util;

import ee.pw.challengeme.infrastructure.security.TokenProvider;
import io.vavr.Tuple2;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CookieService {

	private final TokenProvider tokenProvider;

	public Tuple2<String, String> createUserTokens(
		UserDetails userDetails,
		HttpServletResponse httpServletResponse
	) {
		Tuple2<String, String> userTokensTuple = new Tuple2<>(
			tokenProvider.generateShortToken(userDetails),
			tokenProvider.generateRefreshToken(userDetails)
		);

		List<Cookie> cookies = List.of(
			createSecureCookie("short-access-token", userTokensTuple._1),
			createSecureCookie("refresh-access-token", userTokensTuple._2)
		);

		cookies.forEach(httpServletResponse::addCookie);

		return userTokensTuple;
	}

	private Cookie createSecureCookie(String name, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(10000);

		return cookie;
	}
}
