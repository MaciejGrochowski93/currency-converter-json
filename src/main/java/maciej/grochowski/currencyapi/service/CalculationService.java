package maciej.grochowski.currencyapi.service;

import lombok.AllArgsConstructor;
import maciej.grochowski.currencyapi.currency.CurrencyType;
import maciej.grochowski.currencyapi.domain.Rates;
import maciej.grochowski.currencyapi.exception.IncorrectAmount;
import maciej.grochowski.currencyapi.exception.SameCurrenciesException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static maciej.grochowski.currencyapi.currency.CurrencyType.PLN;

@Service
@AllArgsConstructor
public class CalculationService {

    private final NBPService nbpService;
    private final BigDecimal MARGIN_MULTIPLIER = BigDecimal.valueOf(1.02);

    public Rates provideCalculationsAndMessage(CurrencyType sellCurrency, BigDecimal sellAmount, CurrencyType buyCurrency) {
        BigDecimal buyAmount = BigDecimal.ONE;
        if (sellCurrency == PLN || buyCurrency == PLN) {
            buyAmount = customToForeignCurrency(sellCurrency, sellAmount, buyCurrency);
        } else {
            buyAmount = foreignToForeignCurrency(sellCurrency, sellAmount, buyCurrency);
        }
        var exchangeMessage = String.format("%.2f %s can be exchanged for %.2f %s.", sellAmount, sellCurrency, buyAmount, buyCurrency);
        BigDecimal bid = nbpService.getRateOutOfCurrency(sellCurrency).getBid();
        BigDecimal ask = nbpService.getRateOutOfCurrency(buyCurrency).getAsk();
        return new Rates(bid, ask, exchangeMessage);
    }

    public BigDecimal customToForeignCurrency(CurrencyType sellCurrency, BigDecimal sellAmount, CurrencyType buyCurrency) {
        if (sellCurrency == CurrencyType.PLN) {
            BigDecimal askRate = nbpService.getRateOutOfCurrency(buyCurrency).getAsk();
            return sellAmount.divide((askRate.multiply(MARGIN_MULTIPLIER)), 4, RoundingMode.HALF_UP);
        } else {
            BigDecimal bidRate = nbpService.getRateOutOfCurrency(sellCurrency).getBid();
            return sellAmount.multiply(bidRate.divide(MARGIN_MULTIPLIER, 4, RoundingMode.HALF_UP));
        }
    }

    public BigDecimal foreignToForeignCurrency(CurrencyType sellCurrency, BigDecimal sellAmount, CurrencyType buyCurrency) {
        BigDecimal bidRate = nbpService.getRateOutOfCurrency(sellCurrency).getBid();
        BigDecimal bidPrice = bidRate.divide(MARGIN_MULTIPLIER, 4, RoundingMode.HALF_UP);

        BigDecimal askRate = nbpService.getRateOutOfCurrency(buyCurrency).getAsk();
        return sellAmount.multiply(bidPrice.divide((askRate.multiply(MARGIN_MULTIPLIER)), 4, RoundingMode.HALF_UP));
    }

    public void validateAmount(BigDecimal amount) {
        if (amount.scale() > 2 || amount.compareTo(BigDecimal.ZERO) <= 0 || amount.compareTo(BigDecimal.valueOf(10000000)) >= 0) {
            throw new IncorrectAmount("Provided amount must be a positive number, lower than 10 mln, and with maximum of 2 decimal places.");
        }
    }

    public void validateCurrencies(CurrencyType sellCurrency, CurrencyType buyCurrency) {
        if (sellCurrency == buyCurrency) {
            throw new SameCurrenciesException("You cannot exchange Currency with itself.");
        }
    }
}