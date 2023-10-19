package ee.pw.challengeme.domain.media;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
enum MediaType {
	JPG("jpg"),
	GIF("gif"),
	JPEG("jpeg"),
	BMP("bmp");

	private final String extension;
}
