package maciej.grochowski.currencyapi.service;

import maciej.grochowski.currencyapi.currency.CurrencyType;
import maciej.grochowski.currencyapi.domain.Rates;
import maciej.grochowski.currencyapi.exception.IncorrectAmount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CalculationServiceTest {

    @Mock
    private ParsingService parsingService;

    @InjectMocks
    private CalculationService calculationService;

    @ParameterizedTest
    @MethodSource("invalidAmountsProvider")
    void givenInvalidNumberThrowsException(BigDecimal invalidAmount) {
        assertThrows(IncorrectAmount.class,() -> calculationService.validateAmount(invalidAmount));
    }

    private static Stream<Arguments> invalidAmountsProvider() {
        return Stream.of(Arguments.of(BigDecimal.valueOf(-2)), Arguments.of(BigDecimal.valueOf(2.001)), Arguments.of(BigDecimal.valueOf(10000000)));
    }

    @ParameterizedTest
    @MethodSource("validAmountsProvider")
    void givenValidNumberDoesNotThrow(BigDecimal validAmount) {
        assertDoesNotThrow(() -> calculationService.validateAmount(validAmount));
    }

    private static Stream<Arguments> validAmountsProvider() {
        return Stream.of(Arguments.of(BigDecimal.valueOf(0.01)), Arguments.of(BigDecimal.valueOf(15), Arguments.of(BigDecimal.valueOf(1000000))));
    }

    @Test
    void foreignToForeignCorrectResult() {
        // GIVEN
        CurrencyType chf = CurrencyType.CHF;
        CurrencyType eur = CurrencyType.EUR;

        // WHEN
        Mockito.when(parsingService.getRateOutOfCurrency(CurrencyType.CHF)).thenReturn(Rates.builder().bid(BigDecimal.valueOf(4.00)).build());
        Mockito.when(parsingService.getRateOutOfCurrency(CurrencyType.EUR)).thenReturn(Rates.builder().ask(BigDecimal.valueOf(5.00)).build());

        BigDecimal actualResult = calculationService.foreignToForeignCurrency(chf, BigDecimal.TEN, eur);

        // THEN
        assertEquals(0, BigDecimal.valueOf(7.689).compareTo(actualResult));
    }

    @Test
    void customToForeignCorrectResult() {
        // GIVEN
        CurrencyType chf = CurrencyType.CHF;
        CurrencyType pln = CurrencyType.PLN;

        // WHEN
        Mockito.when(parsingService.getRateOutOfCurrency(CurrencyType.CHF)).thenReturn(Rates.builder().bid(BigDecimal.valueOf(4.00)).build());

        BigDecimal actualResult = calculationService.customToForeignCurrency(chf, BigDecimal.TEN, pln);

        // THEN
        assertEquals(0, BigDecimal.valueOf(39.216).compareTo(actualResult));
    }
}