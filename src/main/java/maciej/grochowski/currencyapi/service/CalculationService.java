package maciej.grochowski.currencyapi.service;

import lombok.AllArgsConstructor;
import maciej.grochowski.currencyapi.currency.CurrencyType;
import maciej.grochowski.currencyapi.exception.IncorrectAmount;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@AllArgsConstructor
public class CalculationService {

    private final ParsingService parsingService;
    private final BigDecimal MARGIN_MULTIPLIER = BigDecimal.valueOf(1.02);

    public BigDecimal customToForeignCurrency(CurrencyType sellCurrency, BigDecimal amount, CurrencyType buyCurrency) {
        if (sellCurrency == CurrencyType.PLN) {
            BigDecimal askRate = parsingService.getRateOutOfCurrency(buyCurrency).getAsk();
            return amount.divide((askRate.multiply(MARGIN_MULTIPLIER)), 4, RoundingMode.HALF_UP);
        } else {
            BigDecimal bidRate = parsingService.getRateOutOfCurrency(sellCurrency).getBid();
            return amount.multiply(bidRate.divide(MARGIN_MULTIPLIER, 4, RoundingMode.HALF_UP));
        }
    }

    public BigDecimal foreignToForeignCurrency(CurrencyType sellCurrency, BigDecimal amount, CurrencyType buyCurrency) {
        BigDecimal bidRate = parsingService.getRateOutOfCurrency(sellCurrency).getBid();
        BigDecimal bidPrice = bidRate.divide(MARGIN_MULTIPLIER, 4, RoundingMode.HALF_UP);

        BigDecimal askRate = parsingService.getRateOutOfCurrency(buyCurrency).getAsk();
        return amount.multiply(bidPrice.divide((askRate.multiply(MARGIN_MULTIPLIER)), 4, RoundingMode.HALF_UP));
    }

    public void validateAmount(BigDecimal amount) {
        if (amount.scale() > 2 || amount.compareTo(BigDecimal.ZERO) <= 0 || amount.compareTo(BigDecimal.valueOf(10000000)) >= 0) {
            throw new IncorrectAmount("Provided amount must be a positive number, lower than 10 mln, and with maximum of 2 decimal places.");
        }
    }
}