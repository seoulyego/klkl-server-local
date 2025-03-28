package taco.klkl.domain.region.service.country;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taco.klkl.domain.region.dao.country.CountryRepository;
import taco.klkl.domain.region.domain.country.Country;
import taco.klkl.domain.region.dto.response.country.CountryResponse;
import taco.klkl.domain.region.dto.response.country.CountrySimpleResponse;
import taco.klkl.domain.region.exception.country.CountryNotFoundException;

@Slf4j
@Primary
@Service
@Transactional
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

	private final CountryRepository countryRepository;

	@Override
	public List<CountrySimpleResponse> findAllCountriesByPartialString(final String partialString) {
		if (partialString == null || partialString.isEmpty()) {
			return List.of();
		}
		final List<Country> countries = countryRepository.findAllByNameContaining(partialString);
		return countries.stream()
			.map(CountrySimpleResponse::from)
			.toList();
	}

	@Override
	public CountryResponse getCountryById(final Long id) throws CountryNotFoundException {
		final Country country = countryRepository.findById(id)
			.orElseThrow(CountryNotFoundException::new);
		return CountryResponse.from(country);
	}
}
