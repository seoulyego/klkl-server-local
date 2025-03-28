package taco.klkl.domain.search.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import taco.klkl.domain.category.domain.category.Category;
import taco.klkl.domain.category.domain.category.CategoryType;
import taco.klkl.domain.category.domain.subcategory.Subcategory;
import taco.klkl.domain.category.domain.subcategory.SubcategoryType;
import taco.klkl.domain.category.dto.response.category.CategorySimpleResponse;
import taco.klkl.domain.category.dto.response.subcategory.SubcategorySimpleResponse;
import taco.klkl.domain.category.service.category.CategoryService;
import taco.klkl.domain.category.service.subcategory.SubcategoryService;
import taco.klkl.domain.region.domain.city.City;
import taco.klkl.domain.region.domain.city.CityType;
import taco.klkl.domain.region.domain.country.Country;
import taco.klkl.domain.region.domain.country.CountryType;
import taco.klkl.domain.region.domain.currency.Currency;
import taco.klkl.domain.region.domain.region.Region;
import taco.klkl.domain.region.dto.response.city.CitySimpleResponse;
import taco.klkl.domain.region.dto.response.country.CountrySimpleResponse;
import taco.klkl.domain.region.service.city.CityService;
import taco.klkl.domain.region.service.country.CountryService;
import taco.klkl.domain.search.dto.response.SearchResponse;

@ExtendWith(MockitoExtension.class)
class SearchServiceImplTest {

	@InjectMocks
	SearchServiceImpl searchService;

	@Mock
	CountryService countryService;

	@Mock
	CityService cityService;

	@Mock
	CategoryService categoryService;

	@Mock
	SubcategoryService subcategoryService;

	@Mock
	Region region;

	@Mock
	Currency currency;

	private final Country country = Country.of(CountryType.MALAYSIA, region, "wallpaper", currency);
	private final City city = City.of(CityType.BORACAY, country);
	private final Category category = Category.of(CategoryType.CLOTHES);
	private final Subcategory subcategory = Subcategory.of(category, SubcategoryType.MAKEUP);

	@Test
	@DisplayName("SearchService 테스트")
	void testFindSearchResult() {
		// given
		String queryParam = "Test";

		List<CountrySimpleResponse> mockCountries =
			Collections.singletonList(CountrySimpleResponse.from(country));
		List<CitySimpleResponse> mockCities = Collections.singletonList(CitySimpleResponse.from(city));
		List<CategorySimpleResponse> mockCategories = Collections.singletonList(CategorySimpleResponse.from(category));
		List<SubcategorySimpleResponse> mockSubcategories =
			Collections.singletonList(SubcategorySimpleResponse.from(subcategory));

		when(countryService.findAllCountriesByPartialString(any(String.class))).thenReturn(mockCountries);
		when(cityService.findAllCitiesByPartialString(any(String.class))).thenReturn(mockCities);
		when(categoryService.findAllCategoriesByPartialString(any(String.class))).thenReturn(mockCategories);
		when(subcategoryService.findAllSubcategoriesByPartialString(any(String.class))).thenReturn(mockSubcategories);

		// when
		SearchResponse result = searchService.findSearchResult(queryParam);

		// then
		assertThat(result).isNotNull();
		assertThat(result.countries()).isEqualTo(mockCountries);
		assertThat(result.cities()).isEqualTo(mockCities);
		assertThat(result.categories()).isEqualTo(mockCategories);
		assertThat(result.subcategories()).isEqualTo(mockSubcategories);
	}
}
