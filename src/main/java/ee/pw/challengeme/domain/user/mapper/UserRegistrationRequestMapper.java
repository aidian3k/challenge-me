package ee.pw.challengeme.domain.user.mapper;

import ee.pw.challengeme.domain.user.dto.UserRegistrationRequest;
import ee.pw.challengeme.domain.user.entity.User;
import ee.pw.challengeme.infrastructure.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserRegistrationRequestMapper
	extends GenericMapper<User, UserRegistrationRequest> {}
