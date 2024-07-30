package taco.klkl.domain.product.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import taco.klkl.domain.product.dao.ProductRepository;
import taco.klkl.domain.product.domain.Product;
import taco.klkl.domain.product.dto.request.ProductCreateRequestDto;
import taco.klkl.domain.product.dto.response.ProductDetailResponseDto;
import taco.klkl.domain.product.exception.ProductNotFoundException;
import taco.klkl.domain.user.domain.User;
import taco.klkl.global.common.constants.ProductConstants;
import taco.klkl.global.util.UserUtil;

class ProductServiceTest {
	@InjectMocks
	private ProductService productService;

	@Mock
	private ProductRepository productRepository;

	@Mock
	private UserUtil userUtil;

	private User user;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		user = new User();
	}

	@Test
	@DisplayName("ID로 상품 정보를 조회할 때, 상품이 존재하면 ProductDetailResponseDto를 반환해야 한다.")
	void testGetProductInfoById_Success() {
		// given
		Long productId = 1L;
		String name = "맛있는 곤약젤리";
		String description = "탱글탱글 맛있는 곤약젤리";
		String address = "신사이바시 메가돈키호테";
		Integer price = 100;
		Long cityId = 2L;
		Long subcategoryId = 3L;
		Long currencyId = 4L;

		Product product = Product.of(
			user,
			name,
			description,
			address,
			price,
			cityId,
			subcategoryId,
			currencyId
		);
		when(productRepository.findById(productId)).thenReturn(Optional.of(product));

		// when
		ProductDetailResponseDto responseDto = productService.getProductInfoById(productId);

		// then
		assertThat(responseDto).isNotNull();
		assertThat(responseDto.userId()).isEqualTo(user.getId());
		assertThat(responseDto.name()).isEqualTo(name);
		assertThat(responseDto.description()).isEqualTo(description);
		assertThat(responseDto.address()).isEqualTo(address);
		assertThat(responseDto.likeCount()).isEqualTo(ProductConstants.DEFAULT_LIKE_COUNT);
		assertThat(responseDto.price()).isEqualTo(price);
		assertThat(responseDto.cityId()).isEqualTo(cityId);
		assertThat(responseDto.subcategoryId()).isEqualTo(subcategoryId);
		assertThat(responseDto.currencyId()).isEqualTo(currencyId);

		verify(productRepository).findById(productId);
	}

	@Test
	@DisplayName("ID로 상품 정보를 조회할 때, 상품이 존재하지 않으면 ProductNotFoundException을 발생시켜야 한다.")
	void testGetProductInfoById_NotFound() {
		// given
		Long productId = 1L;
		when(productRepository.findById(productId)).thenReturn(Optional.empty());

		// when & then
		ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
			productService.getProductInfoById(productId);
		});

		// 예외가 발생했는지 확인
		assertThat(exception).isNotNull();
		verify(productRepository).findById(productId);
	}

	@Test
	@DisplayName("상품을 생성할 때, 정상적으로 ProductDetailResponseDto를 반환해야 한다.")
	void testCreateProduct_Success() {
		// given
		ProductCreateRequestDto productDto = new ProductCreateRequestDto(
			"맛있는 곤약젤리",
			"탱글탱글 맛있는 곤약젤리",
			2L,
			3L,
			4L,
			"신사이바시 메가돈키호테",
			100
		);

		String name = productDto.name();
		String description = productDto.description();
		String address = productDto.address();
		Integer price = productDto.price();
		Long cityId = productDto.cityId();
		Long subcategoryId = productDto.subcategoryId();
		Long currencyId = productDto.currencyId();

		Product product = Product.of(
			user,
			name,
			description,
			address,
			price,
			cityId,
			subcategoryId,
			currencyId
		);
		when(userUtil.findTestUser()).thenReturn(user);
		when(productRepository.save(any(Product.class))).thenReturn(product);

		// when
		ProductDetailResponseDto responseDto = productService.createProduct(productDto);

		// then
		assertThat(responseDto).isNotNull();
		assertThat(responseDto.userId()).isEqualTo(user.getId());
		assertThat(responseDto.name()).isEqualTo(name);
		assertThat(responseDto.description()).isEqualTo(description);
		assertThat(responseDto.address()).isEqualTo(address);
		assertThat(responseDto.likeCount()).isEqualTo(ProductConstants.DEFAULT_LIKE_COUNT);
		assertThat(responseDto.price()).isEqualTo(price);
		assertThat(responseDto.cityId()).isEqualTo(cityId);
		assertThat(responseDto.subcategoryId()).isEqualTo(subcategoryId);
		assertThat(responseDto.currencyId()).isEqualTo(currencyId);

		verify(productRepository).save(any(Product.class));
	}
}
