package taco.klkl.domain.region.dto.response;

import taco.klkl.domain.region.domain.Country;
import taco.klkl.domain.region.domain.Region;

public record CountryResponseDto(
	Long countryId,
	Region region,
	String name,
	String flag,
	String photo,
	int currencyId
) {
	/**
	 *
	 * @param country
	 * @return CountryResponseDto
	 */
	public static CountryResponseDto from(Country country) {
		return new CountryResponseDto(
			country.getCountryId(),
			country.getRegion(),
			country.getName().getName(),
			country.getFlag(),
			country.getPhoto(),
			country.getCurrencyId());
	}
}
