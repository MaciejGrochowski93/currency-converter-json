package maciej.grochowski.currencyapi.service;

import lombok.AllArgsConstructor;
import maciej.grochowski.currencyapi.currency.CurrencyType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

    public boolean isValidAmount(BigDecimal amount) {
        return amount.scale() <= 2 && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isValidCurrency(CurrencyType currency1, CurrencyType currency2, Class<CurrencyType> currencyEnum) {
        boolean instance = currencyEnum.isInstance(currency1);
        boolean instance2 = currencyEnum.isInstance(currency2);
        return instance && instance2;
    }
}
