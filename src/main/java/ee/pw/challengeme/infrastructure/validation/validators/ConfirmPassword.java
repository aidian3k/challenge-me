package ee.pw.challengeme.infrastructure.validation.validators;

import ee.pw.challengeme.domain.user.dto.UserRegistrationRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = ConfirmPassword.ConfirmPasswordValidator.class)
@Documented
public @interface ConfirmPassword {
	String message() default "Confirm password does not match second password";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class ConfirmPasswordValidator
		implements ConstraintValidator<ConfirmPassword, UserRegistrationRequest> {

		private String message;

		@Override
		public void initialize(ConfirmPassword constraintAnnotation) {
			this.message = constraintAnnotation.message();
			ConstraintValidator.super.initialize(constraintAnnotation);
		}

		@Override
		public boolean isValid(
			UserRegistrationRequest userRegistrationRequest,
			ConstraintValidatorContext context
		) {
			final String password = userRegistrationRequest.getPassword();
			final String confirmPassword = userRegistrationRequest.getConfirmationPassword();
			final boolean arePasswordsTheSame = password.equals(confirmPassword);

			if (!arePasswordsTheSame) {
				context
					.buildConstraintViolationWithTemplate(message)
					.addPropertyNode("confirmationPassword")
					.addConstraintViolation();
			}

			return arePasswordsTheSame;
		}
	}
}
