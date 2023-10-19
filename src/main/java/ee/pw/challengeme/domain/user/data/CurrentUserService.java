package ee.pw.challengeme.domain.user.data;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.nio.file.attribute.UserPrincipal;
import java.util.Optional;

@Component
@RequiredArgsConstructor
class CurrentUserService {

	public UserPrincipal getCurrentUserPrincipal() {
		return Optional
			.ofNullable(
				(UserPrincipal) SecurityContextHolder
					.getContext()
					.getAuthentication()
					.getPrincipal()
			)
			.orElseThrow(() ->
				new UsernameNotFoundException("Principal could not be extracted")
			);
	}
}
