package maciej.grochowski.currencyapi;

import maciej.grochowski.currencyapi.exception.IncorrectAmount;
import maciej.grochowski.currencyapi.service.CalculationService;
import maciej.grochowski.currencyapi.service.RedirectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CurrencyApiTest {

    CalculationService calculationService;
    RedirectionService redirectionService;
    TestInfo testInfo;
    TestReporter testReporter;
    BigDecimal MARGIN_MULTIPLIER = BigDecimal.valueOf(1.02);

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
    void foreignToForeignCurrencyTest() {
        //given
        BigDecimal bidRate = BigDecimal.valueOf(4.00);
        BigDecimal askRate = BigDecimal.valueOf(5.00);

        //when
        BigDecimal expected = BigDecimal.valueOf(0.7689);
        BigDecimal actual = bidRate.divide((askRate.multiply(MARGIN_MULTIPLIER.pow(2))), 4, RoundingMode.HALF_UP);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void customToForeignCurrencyTest() {
        // BUYING FOREIGN CURRENCY, PAYING PLN
        BigDecimal askRate = BigDecimal.valueOf(5.00);
        BigDecimal expectedBuy = BigDecimal.valueOf(0.1961);
        BigDecimal actualBuy = BigDecimal.ONE.divide(askRate.multiply(MARGIN_MULTIPLIER), 4, RoundingMode.HALF_UP);
        assertEquals(expectedBuy, actualBuy);

        // SELLING FOREIGN CURRENCY, RECEIVING PLN
        BigDecimal bidRate = BigDecimal.valueOf(4.00);
        BigDecimal expectedSell = BigDecimal.valueOf(3.9216);
        BigDecimal actualSell = bidRate.divide(MARGIN_MULTIPLIER, 4, RoundingMode.HALF_UP);
        assertEquals(expectedSell, actualSell);
    }

    @Test
    void isValidAmountTest() {
        //parametrised test baeldung
        BigDecimal negativeNumber = BigDecimal.valueOf(-1);
        assertThrows(IncorrectAmount.class, () -> calculationService.validateAmount(negativeNumber));

        assertThrows(IncorrectAmount.class, () -> calculationService.validateAmount(BigDecimal.ZERO));

        BigDecimal threeDecimalPlaces = BigDecimal.valueOf(5.001);
        assertThrows(IncorrectAmount.class, () -> calculationService.validateAmount(threeDecimalPlaces));

        BigDecimal tenMillion = BigDecimal.valueOf(10000000);
        assertThrows(IncorrectAmount.class, () -> calculationService.validateAmount(tenMillion));


        BigDecimal twoDecimalPlaces = BigDecimal.valueOf(0.01);
        assertDoesNotThrow(() -> calculationService.validateAmount(twoDecimalPlaces));

        BigDecimal nineMillionNineNine = BigDecimal.valueOf(9999999);
        assertDoesNotThrow(() -> calculationService.validateAmount(nineMillionNineNine));
    }
}
