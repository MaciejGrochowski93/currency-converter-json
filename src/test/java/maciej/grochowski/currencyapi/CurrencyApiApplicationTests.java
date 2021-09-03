package maciej.grochowski.currencyapi;

import maciej.grochowski.currencyapi.service.CalculationService;
import maciej.grochowski.currencyapi.service.RedirectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CurrencyApiApplicationTests {

	CalculationService calculationService;
	RedirectionService redirectionService;
	TestInfo testInfo;
	TestReporter testReporter;
	RestTemplate restTemplate;

	@BeforeEach
	void init(TestInfo testInfo, TestReporter testReporter) {
		this.testInfo = testInfo;
		this.testReporter = testReporter;
		calculationService = new CalculationService(redirectionService);
	}

	@Test
	void contextLoads() {
	}

	@Test
	void foreignToForeignCurrency() {
		BigDecimal bidRate = BigDecimal.valueOf(4.00);
		BigDecimal askRate = BigDecimal.valueOf(5.00);

		BigDecimal expected = BigDecimal.valueOf(0.8);
		BigDecimal actual = bidRate.divide((askRate), 4, RoundingMode.HALF_UP);
		assertEquals(expected, actual);
	}

}
