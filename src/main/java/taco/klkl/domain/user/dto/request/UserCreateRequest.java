package taco.klkl.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record UserCreateRequest(
	@NotNull(message = "이름은 필수 항목입니다.") String name,
	@NotNull(message = "성별은 필수 항목입니다.") String gender,
	@PositiveOrZero(message = "나이는 0 이상이어야 합니다.") Integer age,
	String profile,
	String description
) {
	public static UserCreateRequest of(
		final String name,
		final String gender,
		final Integer age,
		final String profile,
		final String description
	) {
		return new UserCreateRequest(name, gender, age, profile, description);
	}
}
