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

// jednostkowe
// integracyjne
// automatyczne
@ExtendWith(MockitoExtension.class)
class CalculationServiceTest {

    @Mock
    private RedirectionService redirectionService;

    @InjectMocks
    private CalculationService calculationService;

    @ParameterizedTest(name = "{0}")
    @MethodSource("amountProvider")
    void givenInvalidDataExpectException(BigDecimal amount) {
        assertThrows(IncorrectAmount.class,() -> calculationService.validateAmount(amount));
    }

    private static Stream<Arguments> amountProvider() {
        return Stream.of(Arguments.of(BigDecimal.valueOf(-2)), Arguments.of(BigDecimal.valueOf(2.001)), Arguments.of(BigDecimal.valueOf(10000000)));
    }

    @Test
    void foreignToForeignValidResult() {
        // GIVEN
        CurrencyType chf = CurrencyType.CHF;
        CurrencyType eur = CurrencyType.EUR;

        // WHEN
        Mockito.when(redirectionService.getRateOutOfCurrency(CurrencyType.CHF)).thenReturn(Rates.builder().bid(BigDecimal.valueOf(4.00)).build());
        Mockito.when(redirectionService.getRateOutOfCurrency(CurrencyType.EUR)).thenReturn(Rates.builder().ask(BigDecimal.valueOf(5.00)).build());

        BigDecimal actualResult = calculationService.foreignToForeignCurrency(chf, eur);

        // THEN
        assertEquals(BigDecimal.valueOf(0.7689), actualResult);
    }
}