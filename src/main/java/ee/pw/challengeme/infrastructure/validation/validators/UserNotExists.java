package ee.pw.challengeme.infrastructure.validation.validators;

import ee.pw.challengeme.domain.user.data.UserRepository;
import ee.pw.challengeme.domain.user.dto.UserRegistrationRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UserNotExists.UserNotExistsValidator.class)
public @interface UserNotExists {
	String message() default "User with given data already exists";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@RequiredArgsConstructor
	class UserNotExistsValidator
		implements ConstraintValidator<UserNotExists, UserRegistrationRequest> {

		private final UserRepository userRepository;
		private String customMessage;

		@Override
		public void initialize(UserNotExists constraintAnnotation) {
			this.customMessage = constraintAnnotation.message();
			ConstraintValidator.super.initialize(constraintAnnotation);
		}

		@Override
		public boolean isValid(
			UserRegistrationRequest userRegistrationRequest,
			ConstraintValidatorContext context
		) {
			boolean userExistsInDatabase = userRepository
				.findUserByEmail(userRegistrationRequest.getEmail())
				.isPresent();

			if (userExistsInDatabase) {
				context.disableDefaultConstraintViolation();
				context
					.buildConstraintViolationWithTemplate(customMessage)
					.addPropertyNode("email")
					.addConstraintViolation();
			}

			return !userExistsInDatabase;
		}
	}
}
