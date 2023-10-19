package ee.pw.challengeme.infrastructure.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
class ApplicationExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodValidationException(
		MethodArgumentNotValidException exception
	) {
		Map<String, String> errorsMap = exception
			.getFieldErrors()
			.stream()
			.collect(
				Collectors.toMap(
					FieldError::getField,
					Objects.requireNonNull(
						DefaultMessageSourceResolvable::getDefaultMessage
					)
				)
			);

		return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, String>> handleConstraintViolationException(
		ConstraintViolationException violationException
	) {
		Map<String, String> errorsMap = violationException
			.getConstraintViolations()
			.stream()
			.collect(
				Collectors.toMap(
					constraintViolation ->
						constraintViolation.getRootBeanClass().getName(),
					ConstraintViolation::getMessage
				)
			);

		return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
	}
}
