package ee.pw.challengeme.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {

	private final TokenSecurityProperties tokenSecurityProperties;
	private final Clock clock;

	public String generateShortToken(UserDetails userDetails) {
		return generateTokenForUserDetails(
			tokenSecurityProperties.getShortTokenExpirationLength(),
			userDetails
		);
	}

	public String generateRefreshToken(UserDetails userDetails) {
		return generateTokenForUserDetails(
			tokenSecurityProperties.getRefreshTokenExpirationLength(),
			userDetails
		);
	}

	public String extractUserSubjectFromToken(String userToken) {
		final byte[] secretKey = tokenSecurityProperties
			.getTokenSecretKey()
			.getBytes();

		Jws<Claims> claimsJwt = Jwts
			.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(userToken);

		return claimsJwt.getBody().getSubject();
	}

	public void validateToken(String userToken, UserDetails userDetails) {
		final byte[] secretKey = tokenSecurityProperties
			.getTokenSecretKey()
			.getBytes();
		Claims claimsJws = Jwts
			.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(userToken)
			.getBody();
		final Date expirationDate = claimsJws.getExpiration();
		final Date now = new Date();
		final String userName = claimsJws.getSubject();

		if (now.after(expirationDate)) {
			throw new IllegalStateException("User token has expired");
		} else if (!userName.equals(userDetails.getUsername())) {
			throw new IllegalStateException("UserToken does not equals userDetails");
		}
	}

	private String generateTokenForUserDetails(
		int tokenExpirationTimeInMinutes,
		UserDetails userDetails
	) {
		final String userEmail = userDetails.getUsername();
		final Instant issuedAt = clock.instant();
		final Instant expirationDate = issuedAt.plus(
			tokenExpirationTimeInMinutes,
			ChronoUnit.MINUTES
		);
		final byte[] secretKey = tokenSecurityProperties
			.getTokenSecretKey()
			.getBytes();

		return Jwts
			.builder()
			.setSubject(userEmail)
			.setIssuedAt(Date.from(issuedAt))
			.setExpiration(Date.from(expirationDate))
			.signWith(Keys.hmacShaKeyFor(secretKey), SignatureAlgorithm.HS512)
			.compact();
	}
}
