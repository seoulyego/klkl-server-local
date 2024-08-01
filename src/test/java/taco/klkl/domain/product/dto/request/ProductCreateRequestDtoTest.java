package taco.klkl.domain.product.dto.request;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import taco.klkl.global.common.constants.ProductConstants;
import taco.klkl.global.common.constants.ProductValidationMessages;

class ProductCreateRequestDtoTest {

	private Validator validator;

	@BeforeEach
	void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	@DisplayName("유효한 ProductCreateRequestDto 생성 시 검증 통과")
	void testValidProductCreateRequestDto() {
		ProductCreateRequestDto dto = new ProductCreateRequestDto(
			"Valid Product Name",
			"Valid product description",
			"Valid address",
			100,
			1L,
			2L,
			3L
		);

		Set<ConstraintViolation<ProductCreateRequestDto>> violations = validator.validate(dto);
		assertTrue(violations.isEmpty());
	}

	@Test
	@DisplayName("상품명이 null일 때 검증 실패")
	void testNullProductName() {
		ProductCreateRequestDto dto = new ProductCreateRequestDto(
			null,
			"Valid product description",
			"Valid address",
			100,
			1L,
			2L,
			3L
		);

		Set<ConstraintViolation<ProductCreateRequestDto>> violations = validator.validate(dto);
		assertFalse(violations.isEmpty());

		boolean foundNotNullMessage = violations.stream()
			.anyMatch(violation -> violation.getMessage().equals(ProductValidationMessages.NAME_NOT_NULL));
		assertTrue(foundNotNullMessage, "Expected NAME_NOT_NULL message not found");
	}

	@Test
	@DisplayName("상품명이 빈 문자열일 때 검증 실패")
	void testEmptyProductName() {
		ProductCreateRequestDto dto = new ProductCreateRequestDto(
			"",
			"Valid product description",
			"Valid address",
			100,
			1L,
			2L,
			3L
		);

		Set<ConstraintViolation<ProductCreateRequestDto>> violations = validator.validate(dto);
		assertFalse(violations.isEmpty());

		boolean foundNotBlankMessage = violations.stream()
			.anyMatch(violation -> violation.getMessage().equals(ProductValidationMessages.NAME_NOT_BLANK));
		assertTrue(foundNotBlankMessage, "Expected NAME_NOT_BLANK message not found");
	}

	@ParameterizedTest
	@ValueSource(ints = {101, 200, 1000})
	@DisplayName("상품명이 최대 길이를 초과할 때 검증 실패")
	void testProductNameOverMaxLength(int nameLength) {
		String longName = "a".repeat(nameLength);
		ProductCreateRequestDto dto = new ProductCreateRequestDto(
			longName,
			"Valid product description",
			"Valid address",
			100,
			1L,
			2L,
			3L
		);

		Set<ConstraintViolation<ProductCreateRequestDto>> violations = validator.validate(dto);
		assertFalse(violations.isEmpty());
		assertEquals(ProductValidationMessages.NAME_SIZE, violations.iterator().next().getMessage());
	}

	@Test
	@DisplayName("상품 설명이 null일 때 검증 실패")
	void testNullProductDescription() {
		ProductCreateRequestDto dto = new ProductCreateRequestDto(
			"Valid Product Name",
			null,
			"Valid address",
			100,
			1L,
			2L,
			3L
		);

		Set<ConstraintViolation<ProductCreateRequestDto>> violations = validator.validate(dto);
		assertFalse(violations.isEmpty());

		boolean foundNotNullMessage = violations.stream()
			.anyMatch(violation -> violation.getMessage().equals(ProductValidationMessages.DESCRIPTION_NOT_NULL));
		assertTrue(foundNotNullMessage, "Expected DESCRIPTION_NOT_NULL message not found");
	}

	@Test
	@DisplayName("상품 설명이 빈 문자열일 때 검증 실패")
	void testEmptyProductDescription() {
		ProductCreateRequestDto dto = new ProductCreateRequestDto(
			"Valid Product Name",
			"",
			"Valid address",
			100,
			1L,
			2L,
			3L
		);

		Set<ConstraintViolation<ProductCreateRequestDto>> violations = validator.validate(dto);
		assertFalse(violations.isEmpty());

		boolean foundNotBlankMessage = violations.stream()
			.anyMatch(violation -> violation.getMessage().equals(ProductValidationMessages.DESCRIPTION_NOT_BLANK));
		assertTrue(foundNotBlankMessage, "Expected DESCRIPTION_NOT_BLANK message not found");
	}

	@ParameterizedTest
	@ValueSource(ints = {2001, 3000, 5000})
	@DisplayName("상품 설명이 최대 길이를 초과할 때 검증 실패")
	void testProductDescriptionOverMaxLength(int descriptionLength) {
		String longDescription = "a".repeat(descriptionLength);
		ProductCreateRequestDto dto = new ProductCreateRequestDto(
			"Valid Product Name",
			longDescription,
			"Valid address",
			100,
			1L,
			2L,
			3L
		);

		Set<ConstraintViolation<ProductCreateRequestDto>> violations = validator.validate(dto);
		assertFalse(violations.isEmpty());
		assertEquals(ProductValidationMessages.DESCRIPTION_SIZE, violations.iterator().next().getMessage());
	}

	@Test
	@DisplayName("주소가 null일 때 검증 실패")
	void testNullAddress() {
		ProductCreateRequestDto dto = new ProductCreateRequestDto(
			"Valid Product Name",
			"Valid product description",
			null,
			100,
			1L,
			2L,
			3L
		);

		Set<ConstraintViolation<ProductCreateRequestDto>> violations = validator.validate(dto);
		assertFalse(violations.isEmpty());

		boolean foundNotNullMessage = violations.stream()
			.anyMatch(violation -> violation.getMessage().equals(ProductValidationMessages.ADDRESS_NOT_NULL));
		assertTrue(foundNotNullMessage, "Expected ADDRESS_NOT_NULL message not found");
	}

	@Test
	@DisplayName("주소가 빈 문자열일 때 검증 통과")
	void testEmptyAddress() {
		ProductCreateRequestDto dto = new ProductCreateRequestDto(
			"Valid Product Name",
			"Valid product description",
			"",
			100,
			1L,
			2L,
			3L
		);

		Set<ConstraintViolation<ProductCreateRequestDto>> violations = validator.validate(dto);
		assertTrue(violations.isEmpty());
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 1, ProductConstants.ADDRESS_MAX_LENGTH})
	@DisplayName("주소가 최대 길이 이하일 때 검증 통과")
	void testAddressUnderMaxLength(int addressLength) {
		String address = "a".repeat(addressLength);
		ProductCreateRequestDto dto = new ProductCreateRequestDto(
			"Valid Product Name",
			"Valid product description",
			address,
			100,
			1L,
			2L,
			3L
		);

		Set<ConstraintViolation<ProductCreateRequestDto>> violations = validator.validate(dto);
		assertTrue(violations.isEmpty());
	}

	@ParameterizedTest
	@ValueSource(ints = {101, 200, 500})
	@DisplayName("주소가 최대 길이를 초과할 때 검증 실패")
	void testAddressOverMaxLength(int addressLength) {
		String longAddress = "a".repeat(addressLength);
		ProductCreateRequestDto dto = new ProductCreateRequestDto(
			"Valid Product Name",
			"Valid product description",
			longAddress,
			100,
			1L,
			2L,
			3L
		);

		Set<ConstraintViolation<ProductCreateRequestDto>> violations = validator.validate(dto);
		assertFalse(violations.isEmpty());
		assertEquals(ProductValidationMessages.ADDRESS_SIZE, violations.iterator().next().getMessage());
	}

	@Test
	@DisplayName("가격이 null일 때 검증 실패")
	void testNullPrice() {
		ProductCreateRequestDto dto = new ProductCreateRequestDto(
			"Valid Product Name",
			"Valid product description",
			"Valid address",
			null,
			1L,
			2L,
			3L
		);

		Set<ConstraintViolation<ProductCreateRequestDto>> violations = validator.validate(dto);
		assertFalse(violations.isEmpty());

		boolean foundNotNullMessage = violations.stream()
			.anyMatch(violation -> violation.getMessage().equals(ProductValidationMessages.PRICE_NOT_NULL));
		assertTrue(foundNotNullMessage, "Expected PRICE_NOT_NULL message not found");
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 100, 1000})
	@DisplayName("가격이 0 이상일 때 검증 통과")
	void testZeroOrPositivePrice() {
		ProductCreateRequestDto dto = new ProductCreateRequestDto(
			"Valid Product Name",
			"Valid product description",
			"Valid address",
			0,
			1L,
			2L,
			3L
		);

		Set<ConstraintViolation<ProductCreateRequestDto>> violations = validator.validate(dto);
		assertTrue(violations.isEmpty());
	}

	@ParameterizedTest
	@ValueSource(ints = {-1, -100, -1000})
	@DisplayName("가격이 음수일 때 검증 실패")
	void testNegativePrice(int price) {
		ProductCreateRequestDto dto = new ProductCreateRequestDto(
			"Valid Product Name",
			"Valid product description",
			"Valid address",
			price,
			1L,
			2L,
			3L
		);

		Set<ConstraintViolation<ProductCreateRequestDto>> violations = validator.validate(dto);
		assertFalse(violations.isEmpty());
		assertEquals(ProductValidationMessages.PRICE_POSITIVE_OR_ZERO, violations.iterator().next().getMessage());
	}

	@Test
	@DisplayName("도시 ID가 null일 때 검증 실패")
	void testNullCityId() {
		ProductCreateRequestDto dto = new ProductCreateRequestDto(
			"Valid Product Name",
			"Valid product description",
			"Valid address",
			100,
			null,
			2L,
			3L
		);

		Set<ConstraintViolation<ProductCreateRequestDto>> violations = validator.validate(dto);
		assertFalse(violations.isEmpty());
		assertEquals(ProductValidationMessages.CITY_ID_NOT_NULL, violations.iterator().next().getMessage());
	}

	@Test
	@DisplayName("상품 소분류 ID가 null일 때 검증 실패")
	void testNullSubcategoryId() {
		ProductCreateRequestDto dto = new ProductCreateRequestDto(
			"Valid Product Name",
			"Valid product description",
			"Valid address",
			100,
			1L,
			null,
			3L
		);

		Set<ConstraintViolation<ProductCreateRequestDto>> violations = validator.validate(dto);
		assertFalse(violations.isEmpty());
		assertEquals(ProductValidationMessages.SUBCATEGORY_ID_NOT_NULL, violations.iterator().next().getMessage());
	}

	@Test
	@DisplayName("통화 ID가 null일 때 검증 실패")
	void testNullCurrencyId() {
		ProductCreateRequestDto dto = new ProductCreateRequestDto(
			"Valid Product Name",
			"Valid product description",
			"Valid address",
			100,
			1L,
			2L,
			null
		);

		Set<ConstraintViolation<ProductCreateRequestDto>> violations = validator.validate(dto);
		assertFalse(violations.isEmpty());
		assertEquals(ProductValidationMessages.CURRENCY_ID_NOT_NULL, violations.iterator().next().getMessage());
	}
}
