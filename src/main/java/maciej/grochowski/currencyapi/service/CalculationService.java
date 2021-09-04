package maciej.grochowski.currencyapi.service;

import lombok.AllArgsConstructor;
import maciej.grochowski.currencyapi.currency.CurrencyType;
import maciej.grochowski.currencyapi.exception.IncorrectAmount;
//import maciej.grochowski.currencyapi.exception.InvalidCurrency;
import org.springframework.stereotype.Service;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CalculationService {

    private final RedirectionService redirectionService;
    private final BigDecimal MARGIN_MULTIPLIER = BigDecimal.valueOf(1.02);

    public BigDecimal customToForeignCurrency(CurrencyType sellCurrency, CurrencyType buyCurrency) {
        if (sellCurrency.name().equals("PLN")) {
            BigDecimal askRate = redirectionService.getRateOutOfCurrency(buyCurrency).getAsk();
            return BigDecimal.ONE.divide((askRate.multiply(MARGIN_MULTIPLIER)), 4, RoundingMode.HALF_UP);
        } else {
            BigDecimal bidRate = redirectionService.getRateOutOfCurrency(sellCurrency).getBid();
            return bidRate.divide(MARGIN_MULTIPLIER, 4, RoundingMode.HALF_UP);
        }
    }

    public BigDecimal foreignToForeignCurrency(CurrencyType sellCurrency, CurrencyType buyCurrency) {
        BigDecimal bidRate = redirectionService.getRateOutOfCurrency(sellCurrency).getBid();
        BigDecimal bidPrice = bidRate.divide(MARGIN_MULTIPLIER, 4, RoundingMode.HALF_UP);

        BigDecimal askRate = redirectionService.getRateOutOfCurrency(buyCurrency).getAsk();
        return bidPrice.divide((askRate.multiply(MARGIN_MULTIPLIER)), 4, RoundingMode.HALF_UP);
    }

//    public boolean isValidAmount(BigDecimal amount) {
//            return amount.scale() <= 2 && amount.compareTo(BigDecimal.ZERO) > 0;
//    }

    public boolean isValidAmount(BigDecimal amount) {
        if (amount.scale() <= 2 && amount.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(BigDecimal.valueOf(10000000)) < 0) {
            return true;
        } throw new IncorrectAmount("Provided amount must be a positive number, lower than 10 mln, and with maximum of 2 decimal places.");
    }

    public boolean isValidCurrency(CurrencyType currency1, CurrencyType currency2) {
        List<CurrencyType> currencyList = Arrays.stream(CurrencyType.values())
                .filter(currency -> currency.equals(currency1) || currency.equals(currency2))
                .collect(Collectors.toList());
        if (currencyList.contains(currency1) && currencyList.contains(currency2)) {
            return true;
        }
        else {
            return false;
//            throw new InvalidCurrency("Provided Currency is incorrect - check CurrencyType to see proper values.");
        }
    }
}