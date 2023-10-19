package ee.pw.challengeme.domain.user.data;

import ee.pw.challengeme.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserFinderService {

	private final UserRepository userRepository;

	public User getUserByEmail(String userEmail) {
		return userRepository
			.findUserByEmail(userEmail)
			.orElseThrow(() -> new UsernameNotFoundException(userEmail));
	}
}
