package ee.pw.challengeme.domain.user.dto;

import lombok.Builder;

@Builder
public record UserLoginResponse(
	Long id,
	String email,
	String name,
	String userName
) {}
