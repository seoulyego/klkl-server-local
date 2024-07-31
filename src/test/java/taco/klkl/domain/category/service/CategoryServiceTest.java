package taco.klkl.domain.category.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.transaction.Transactional;
import taco.klkl.domain.category.dao.CategoryRepository;
import taco.klkl.domain.category.domain.Category;
import taco.klkl.domain.category.domain.CategoryName;
import taco.klkl.domain.category.domain.Subcategory;
import taco.klkl.domain.category.domain.SubcategoryName;
import taco.klkl.domain.category.dto.response.CategoryResponseDto;
import taco.klkl.domain.category.dto.response.CategoryWithSubcategoryDto;
import taco.klkl.domain.category.exception.CategoryNotFoundException;

@ExtendWith(MockitoExtension.class)
@Transactional
class CategoryServiceTest {

	@Mock
	private CategoryRepository categoryRepository;

	@InjectMocks
	private CategoryService categoryService;

	private final Category category = Category.of(CategoryName.FOOD);
	private final Subcategory subcategory1 = Subcategory.of(category, SubcategoryName.DRESS);
	private final Subcategory subcategory2 = Subcategory.of(category, SubcategoryName.HAIR_CARE);
	private final List<Subcategory> subcategories = Arrays.asList(subcategory1, subcategory2);

	@Test
	@DisplayName("카테고리 Service CategoryResponse(DTO)에 담겨 나오는지 Test")
	void testGetCategories() {
		// given
		Category category1 = Category.of(CategoryName.CLOTHES);
		Category category2 = Category.of(CategoryName.FOOD);
		List<Category> categories = Arrays.asList(category1, category2);

		when(categoryRepository.findAll()).thenReturn(categories);

		// when
		List<CategoryResponseDto> result = categoryService.getCategories();

		// then
		assertNotNull(result);
		assertEquals(2, result.size());

		assertEquals(CategoryName.CLOTHES.getKoreanName(), result.get(0).category());
		assertEquals(CategoryName.FOOD.getKoreanName(), result.get(1).category());

		verify(categoryRepository, times(1)).findAll();
	}

	@Test
	@DisplayName("Valid한 카테고리ID 입력시 해당하는 서브카테고리를 반환하는지 테스트")
	void testGetSubcategoriesWithValidCategoryId() {
		//given
		Long categoryId = 1L;
		Category mockCategory = mock(Category.class);

		//when
		when(mockCategory.getSubcategories()).thenReturn(subcategories);
		when(mockCategory.getName()).thenReturn(CategoryName.FOOD);
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(mockCategory));
		CategoryWithSubcategoryDto response = categoryService.getSubcategories(categoryId);

		//then
		assertNotNull(response);
		System.out.println(response);
		assertEquals(SubcategoryName.DRESS.getName(), response.subcategories().get(0).subcategory());
		assertEquals(SubcategoryName.HAIR_CARE.getName(), response.subcategories().get(1).subcategory());

		verify(categoryRepository, times(1)).findById(1L);
	}

	@Test
	@DisplayName("Invalid한 카테고리 ID 입력시 CategoryNotFoundException을 반환하는지 테스트")
	void testGetSubcategoriesWithInvalidCategoryId() {
		//given
		Long categoryId = 1L;

		//when
		when(categoryRepository.findById(categoryId)).thenThrow(CategoryNotFoundException.class);

		//then
		assertThrows(CategoryNotFoundException.class, () -> {
			categoryService.getSubcategories(categoryId);
		});

		verify(categoryRepository, times(1)).findById(categoryId);
	}
}
