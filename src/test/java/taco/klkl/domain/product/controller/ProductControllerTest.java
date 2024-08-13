package taco.klkl.domain.product.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import taco.klkl.domain.category.domain.CategoryName;
import taco.klkl.domain.category.dto.response.FilterResponseDto;
import taco.klkl.domain.category.dto.response.SubcategoryResponseDto;
import taco.klkl.domain.product.dto.request.ProductCreateUpdateRequestDto;
import taco.klkl.domain.product.dto.request.ProductFilterOptionsDto;
import taco.klkl.domain.product.dto.response.ProductDetailResponseDto;
import taco.klkl.domain.product.dto.response.ProductSimpleResponseDto;
import taco.klkl.domain.product.service.ProductService;
import taco.klkl.domain.region.dto.response.CityResponseDto;
import taco.klkl.domain.region.dto.response.CurrencyResponseDto;
import taco.klkl.domain.region.enums.CountryType;
import taco.klkl.domain.user.dto.response.UserDetailResponseDto;
import taco.klkl.global.common.response.PagedResponseDto;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@Autowired
	private ObjectMapper objectMapper;

	private ProductSimpleResponseDto productSimpleResponseDto;
	private ProductDetailResponseDto productDetailResponseDto;
	private ProductCreateUpdateRequestDto productCreateUpdateRequestDto;

	@BeforeEach
	void setUp() {
		UserDetailResponseDto userDetailResponseDto = new UserDetailResponseDto(
			1L,
			"image/profile.jpg",
			"userName",
			"userDescription",
			100
		);
		CityResponseDto cityResponseDto = new CityResponseDto(
			1L,
			"cityName"
		);
		SubcategoryResponseDto subcategoryResponseDto = new SubcategoryResponseDto(
			1L,
			"subcategoryName"
		);
		CurrencyResponseDto currencyResponseDto = new CurrencyResponseDto(
			1L,
			"currencyCode",
			"image/flag.jpg"
		);
		FilterResponseDto filterResponseDto1 = new FilterResponseDto(
			1L,
			"filterName1"
		);
		FilterResponseDto filterResponseDto2 = new FilterResponseDto(
			2L,
			"filterName2"
		);

		productSimpleResponseDto = new ProductSimpleResponseDto(
			1L,
			"productName",
			10,
			CountryType.THAILAND.getKoreanName(),
			CategoryName.FOOD.getKoreanName()
		);
		productDetailResponseDto = new ProductDetailResponseDto(
			1L,
			"productName",
			"Description",
			"123 Street",
			1000,
			10,
			userDetailResponseDto,
			cityResponseDto,
			subcategoryResponseDto,
			currencyResponseDto,
			Set.of(filterResponseDto1, filterResponseDto2),
			LocalDateTime.now()
		);
		productCreateUpdateRequestDto = new ProductCreateUpdateRequestDto(
			"productName",
			"productDescription",
			"productAddress",
			1000,
			1L,
			1L,
			1L,
			Set.of(1L, 2L)
		);
	}

	@Test
	@DisplayName("상품 목록 조회 - 성공")
	void testGetProducts_ShouldReturnPagedProducts() throws Exception {
		// Given
		List<ProductSimpleResponseDto> products = List.of(productSimpleResponseDto);
		PagedResponseDto<ProductSimpleResponseDto> pagedResponse = new PagedResponseDto<>(
			products, 0, 10, 1, 1, true
		);
		when(productService.getProductsByFilterOptions(any(Pageable.class), any(ProductFilterOptionsDto.class)))
			.thenReturn(pagedResponse);

		// When & Then
		mockMvc.perform(get("/v1/products")
				.param("page", "0")
				.param("size", "10")
				.param("country_id", "1", "2", "3") // Add countryIds as query parameters
				.param("city_id", "4", "5"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.content", hasSize(1)))
			.andExpect(jsonPath("$.data.content[0].id",
				is(productSimpleResponseDto.id().intValue())))
			.andExpect(jsonPath("$.data.content[0].name", is(productSimpleResponseDto.name())))
			.andExpect(jsonPath("$.data.content[0].likeCount", is(productSimpleResponseDto.likeCount())))
			.andExpect(jsonPath("$.data.content[0].countryName", is(productSimpleResponseDto.countryName())))
			.andExpect(jsonPath("$.data.content[0].categoryName",
				is(productSimpleResponseDto.categoryName())))
			.andExpect(jsonPath("$.data.pageNumber", is(0)))
			.andExpect(jsonPath("$.data.pageSize", is(10)))
			.andExpect(jsonPath("$.data.totalElements", is(1)))
			.andExpect(jsonPath("$.data.totalPages", is(1)))
			.andExpect(jsonPath("$.data.last", is(true)))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("상품 상세 조회 - 성공")
	void testGetProductById_ShouldReturnProduct() throws Exception {
		// Given
		when(productService.getProductById(1L)).thenReturn(productDetailResponseDto);

		// When & Then
		mockMvc.perform(get("/v1/products/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.id", is(productDetailResponseDto.id().intValue())))
			.andExpect(jsonPath("$.data.name", is(productDetailResponseDto.name())))
			.andExpect(jsonPath("$.data.description", is(productDetailResponseDto.description())))
			.andExpect(jsonPath("$.data.address", is(productDetailResponseDto.address())))
			.andExpect(jsonPath("$.data.price", is(productDetailResponseDto.price())))
			.andExpect(jsonPath("$.data.likeCount", is(productDetailResponseDto.likeCount())))
			.andExpect(jsonPath("$.data.user.id", is(productDetailResponseDto.user().id().intValue())))
			.andExpect(jsonPath("$.data.user.profile", is(productDetailResponseDto.user().profile())))
			.andExpect(jsonPath("$.data.user.name", is(productDetailResponseDto.user().name())))
			.andExpect(jsonPath("$.data.user.description",
				is(productDetailResponseDto.user().description())))
			.andExpect(jsonPath("$.data.user.totalLikeCount",
				is(productDetailResponseDto.user().totalLikeCount())))
			.andExpect(jsonPath("$.data.city.cityId",
				is(productDetailResponseDto.city().cityId().intValue())))
			.andExpect(jsonPath("$.data.city.name", is(productDetailResponseDto.city().name())))
			.andExpect(jsonPath("$.data.subcategory.subcategoryId",
				is(productDetailResponseDto.subcategory().subcategoryId().intValue())))
			.andExpect(jsonPath("$.data.subcategory.subcategory",
				is(productDetailResponseDto.subcategory().subcategory())))
			.andExpect(jsonPath("$.data.currency.currencyId",
				is(productDetailResponseDto.currency().currencyId().intValue())))
			.andExpect(jsonPath("$.data.currency.code", is(productDetailResponseDto.currency().code())))
			.andExpect(jsonPath("$.data.currency.flag", is(productDetailResponseDto.currency().flag())))
			.andExpect(jsonPath("$.data.createdAt", notNullValue()))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("상품 등록 - 성공")
	void testCreateProduct_ShouldReturnCreatedProduct() throws Exception {
		// Given
		when(productService.createProduct(any(ProductCreateUpdateRequestDto.class)))
			.thenReturn(productDetailResponseDto);

		// When & Then
		mockMvc.perform(post("/v1/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productCreateUpdateRequestDto)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.id", is(productDetailResponseDto.id().intValue())))
			.andExpect(jsonPath("$.data.name", is(productDetailResponseDto.name())))
			.andExpect(jsonPath("$.data.description", is(productDetailResponseDto.description())))
			.andExpect(jsonPath("$.data.address", is(productDetailResponseDto.address())))
			.andExpect(jsonPath("$.data.price", is(productDetailResponseDto.price())))
			.andExpect(jsonPath("$.data.likeCount", is(productDetailResponseDto.likeCount())))
			.andExpect(jsonPath("$.data.user.id", is(productDetailResponseDto.user().id().intValue())))
			.andExpect(jsonPath("$.data.user.profile", is(productDetailResponseDto.user().profile())))
			.andExpect(jsonPath("$.data.user.name", is(productDetailResponseDto.user().name())))
			.andExpect(jsonPath("$.data.user.description",
				is(productDetailResponseDto.user().description())))
			.andExpect(jsonPath("$.data.user.totalLikeCount",
				is(productDetailResponseDto.user().totalLikeCount())))
			.andExpect(jsonPath("$.data.city.cityId",
				is(productDetailResponseDto.city().cityId().intValue())))
			.andExpect(jsonPath("$.data.city.name", is(productDetailResponseDto.city().name())))
			.andExpect(jsonPath("$.data.subcategory.subcategoryId",
				is(productDetailResponseDto.subcategory().subcategoryId().intValue())))
			.andExpect(jsonPath("$.data.subcategory.subcategory",
				is(productDetailResponseDto.subcategory().subcategory())))
			.andExpect(jsonPath("$.data.currency.currencyId",
				is(productDetailResponseDto.currency().currencyId().intValue())))
			.andExpect(jsonPath("$.data.currency.code", is(productDetailResponseDto.currency().code())))
			.andExpect(jsonPath("$.data.currency.flag", is(productDetailResponseDto.currency().flag())))
			.andExpect(jsonPath("$.data.createdAt", notNullValue()))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("상품 정보 수정 - 성공")
	void testUpdateProduct_ShouldReturnUpdatedProduct() throws Exception {
		// Given
		when(productService.updateProduct(eq(1L), any(ProductCreateUpdateRequestDto.class)))
			.thenReturn(productDetailResponseDto);

		// When & Then
		mockMvc.perform(put("/v1/products/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productCreateUpdateRequestDto)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data.id", is(productDetailResponseDto.id().intValue())))
			.andExpect(jsonPath("$.data.name", is(productDetailResponseDto.name())))
			.andExpect(jsonPath("$.data.description", is(productDetailResponseDto.description())))
			.andExpect(jsonPath("$.data.address", is(productDetailResponseDto.address())))
			.andExpect(jsonPath("$.data.price", is(productDetailResponseDto.price())))
			.andExpect(jsonPath("$.data.likeCount", is(productDetailResponseDto.likeCount())))
			.andExpect(jsonPath("$.data.user.id", is(productDetailResponseDto.user().id().intValue())))
			.andExpect(jsonPath("$.data.user.profile", is(productDetailResponseDto.user().profile())))
			.andExpect(jsonPath("$.data.user.name", is(productDetailResponseDto.user().name())))
			.andExpect(jsonPath("$.data.user.description",
				is(productDetailResponseDto.user().description())))
			.andExpect(jsonPath("$.data.user.totalLikeCount",
				is(productDetailResponseDto.user().totalLikeCount())))
			.andExpect(jsonPath("$.data.city.cityId",
				is(productDetailResponseDto.city().cityId().intValue())))
			.andExpect(jsonPath("$.data.city.name", is(productDetailResponseDto.city().name())))
			.andExpect(jsonPath("$.data.subcategory.subcategoryId",
				is(productDetailResponseDto.subcategory().subcategoryId().intValue())))
			.andExpect(jsonPath("$.data.subcategory.subcategory",
				is(productDetailResponseDto.subcategory().subcategory())))
			.andExpect(jsonPath("$.data.currency.currencyId",
				is(productDetailResponseDto.currency().currencyId().intValue())))
			.andExpect(jsonPath("$.data.currency.code", is(productDetailResponseDto.currency().code())))
			.andExpect(jsonPath("$.data.currency.flag", is(productDetailResponseDto.currency().flag())))
			.andExpect(jsonPath("$.data.createdAt", notNullValue()))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}

	@Test
	@DisplayName("상품 삭제 - 성공")
	void testDeleteProduct_ShouldReturnNoContent() throws Exception {
		// Given
		doNothing().when(productService).deleteProduct(1L);

		// When & Then
		mockMvc.perform(delete("/v1/products/1"))
			.andExpect(status().isNoContent())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.code", is("C000")))
			.andExpect(jsonPath("$.data", nullValue()))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}
}
