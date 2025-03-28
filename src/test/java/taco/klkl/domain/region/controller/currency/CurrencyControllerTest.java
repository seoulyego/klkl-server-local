package taco.klkl.domain.region.controller.currency;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import taco.klkl.domain.region.dao.currency.CurrencyRepository;
import taco.klkl.domain.region.domain.currency.Currency;
import taco.klkl.domain.region.domain.currency.CurrencyType;
import taco.klkl.domain.region.dto.response.currency.CurrencyResponse;
import taco.klkl.domain.region.service.currency.CurrencyService;
import taco.klkl.domain.token.service.TokenProvider;
import taco.klkl.global.config.security.TestSecurityConfig;
import taco.klkl.global.util.ResponseUtil;
import taco.klkl.global.util.TokenUtil;

@WebMvcTest(CurrencyController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class CurrencyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TokenProvider tokenProvider;

	@MockBean
	private ResponseUtil responseUtil;

	@MockBean
	private TokenUtil tokenUtil;

	@MockBean
	CurrencyService currencyService;

	@MockBean
	CurrencyRepository currencyRepository;

	private final Currency currency1 = Currency.of(CurrencyType.JAPANESE_YEN);
	private final Currency currency2 = Currency.of(CurrencyType.THAI_BAHT);
	private final CurrencyResponse currencyResponse1 = CurrencyResponse.from(currency1);
	private final CurrencyResponse currencyResponse2 = CurrencyResponse.from(currency2);

	@Test
	@DisplayName("모든 통화 목록 조회")
	void testGetAllCurrency() throws Exception {
		// given
		List<CurrencyResponse> currencyResponseList = Arrays.asList(currencyResponse1, currencyResponse2);
		when(currencyService.findAllCurrencies()).thenReturn(currencyResponseList);

		// when & then
		mockMvc.perform(get("/v1/currencies")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isSuccess", is(true)))
			.andExpect(jsonPath("$.data", hasSize(2)))
			.andExpect(jsonPath("$.data[0].code", is(currency1.getCode())))
			.andExpect(jsonPath("$.data[1].code", is(currency2.getCode())))
			.andExpect(jsonPath("$.timestamp", notNullValue()));
	}
}
