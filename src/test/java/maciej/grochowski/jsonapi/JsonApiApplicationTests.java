package maciej.grochowski.jsonapi;

import maciej.grochowski.jsonapi.service.MoneyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class JsonApiApplicationTests {

	MoneyService service;
	TestInfo testInfo;
	TestReporter testReporter;
	RestTemplate restTemplate;

	@BeforeEach
	void init(TestInfo testInfo, TestReporter testReporter) {
		this.testInfo = testInfo;
		this.testReporter = testReporter;
//		restTemplate = new RestTemplate();
		service = new MoneyService(restTemplate);
	}

	@Test
	void contextLoads() {
	}

	@Test
	void customToForeignCurrencyTest() {
		String buy = "PLN";

	}

}
