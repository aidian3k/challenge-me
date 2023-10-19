package ee.pw.challengeme.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class UserRegistrationResponse {

	private Long id;
	private String email;
	private String name;
	private String userName;
}
