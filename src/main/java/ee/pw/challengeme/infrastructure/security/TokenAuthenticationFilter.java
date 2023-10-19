package ee.pw.challengeme.infrastructure.security;

import io.vavr.control.Either;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private static final String JWT_TOKEN_HEADER = "Authorization";
	private static final String JWT_TOKEN_PREFIX = "Bearer ";

	private final TokenProvider tokenProvider;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain
	) throws ServletException, IOException {
		final Either<Throwable, String> tokenEither = getJwtFromUserRequest(
			request
		);

		if (tokenEither.isLeft()) {
			log.debug("Token is not present in the request: doing filter");
			filterChain.doFilter(request, response);
			return;
		}

		final String userToken = tokenEither.get();

		final String userEmail = tokenProvider.extractUserSubjectFromToken(
			userToken
		);

		try {
			final UserDetails userDetails = userDetailsService.loadUserByUsername(
				userEmail
			);
			final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				userDetails,
				null,
				userDetails.getAuthorities()
			);
			tokenProvider.validateToken(userToken, userDetails);

			authentication.setDetails(
				new WebAuthenticationDetailsSource().buildDetails(request)
			);

			SecurityContextHolder.getContext().setAuthentication(authentication);

			log.debug("Successfuly authenticated user with email=[{}]", userEmail);
		} catch (Exception exception) {
			log.error("Cannot set user authenticated!", exception);
		}

		filterChain.doFilter(request, response);
	}

	@NonNull
	private Either<Throwable, String> getJwtFromUserRequest(
		@NonNull HttpServletRequest request
	) {
		final String userToken = request.getHeader(JWT_TOKEN_HEADER);

		if (userToken != null && userToken.startsWith(JWT_TOKEN_PREFIX)) {
			return Either.right(userToken.replace(JWT_TOKEN_PREFIX, ""));
		}

		return Either.left(new IllegalStateException("Token is not valid"));
	}
}
