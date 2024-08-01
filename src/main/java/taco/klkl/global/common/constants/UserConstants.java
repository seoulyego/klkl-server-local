package taco.klkl.global.common.constants;

import taco.klkl.domain.user.domain.User;
import taco.klkl.global.common.enums.Gender;

public final class UserConstants {

	public static final String TEST_USER_NAME = "testUser";
	public static final User TEST_USER = User.of(
		"image/test.jpg",
		TEST_USER_NAME,
		Gender.MALE,
		20,
		"테스트입니다."
	);

	public static final String DEFAULT_PROFILE = "image/test.jpg";
	public static final int DEFAULT_TOTAL_LIKE_COUNT = 0;

	private UserConstants() {
	}
}
