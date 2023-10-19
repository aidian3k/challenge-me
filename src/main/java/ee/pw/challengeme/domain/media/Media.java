package ee.pw.challengeme.domain.media;


import ee.pw.challengeme.infrastructure.validation.constants.ValidationConstants;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "medias")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class Media {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "media_seq")
	@SequenceGenerator(name = "media_seq", allocationSize = 1)
	private Long id;

	@NotNull
	@Size(max = ValidationConstants.TEN_BITS_SIZE)
	private String name;

	@NotNull
	private String objectKey;

	@Enumerated(EnumType.STRING)
	@NotNull
	private MediaType extension;

	@CreationTimestamp
	private LocalDateTime createdAt;
}
