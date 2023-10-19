package ee.pw.challengeme.infrastructure.validation.validators;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Predicate;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Password.PasswordValidator.class)
public @interface Password {
	String message() default "Given password is invalid";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class PasswordValidator implements ConstraintValidator<Password, String> {

		private String message;

		@Override
		public void initialize(Password constraintAnnotation) {
			this.message = constraintAnnotation.message();
			ConstraintValidator.super.initialize(constraintAnnotation);
		}

		@Override
		public boolean isValid(
			String password,
			ConstraintValidatorContext context
		) {
			boolean isPasswordMatchesCriteria = passwordValidPredicate()
				.test(password);

			if (!isPasswordMatchesCriteria) {
				context
					.buildConstraintViolationWithTemplate(message)
					.addPropertyNode("password")
					.addConstraintViolation();
			}

			return isPasswordMatchesCriteria;
		}

		private Predicate<String> passwordValidPredicate() {
			return password -> password.matches("^.{3,}$");
		}
	}
}
