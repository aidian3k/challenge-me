package ee.pw.challengeme.domain.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import ee.pw.challengeme.infrastructure.validation.validators.ConfirmPassword;
import ee.pw.challengeme.infrastructure.validation.validators.Password;
import ee.pw.challengeme.infrastructure.validation.validators.UserNotExists;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@ConfirmPassword
@UserNotExists
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserRegistrationRequest {

	@Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
	@Size(min = 2, max = 255)
	@NotNull
	private String email;

	@Size(min = 1, max = 5)
	@Password
	private String password;

	@Size(min = 1, max = 255)
	@NotNull
	private String name;

	private String confirmationPassword;

	@Size(min = 1, max = 255)
	private String userName;

	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDateTime birthdayDate;
}
