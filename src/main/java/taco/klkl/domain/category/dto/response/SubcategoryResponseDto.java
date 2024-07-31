package taco.klkl.domain.category.dto.response;

import taco.klkl.domain.category.domain.Subcategory;

public record SubcategoryResponseDto(
	Long subcategoryId,
	String subcategory
) {
	public static SubcategoryResponseDto from(Subcategory subcategory) {
		return new SubcategoryResponseDto(subcategory.getId(), subcategory.getName().getKoreanName());
	}
}
