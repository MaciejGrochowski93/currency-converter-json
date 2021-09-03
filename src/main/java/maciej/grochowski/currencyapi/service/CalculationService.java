package maciej.grochowski.currencyapi.service;

import lombok.AllArgsConstructor;
import maciej.grochowski.currencyapi.currency.CurrencyType;
import maciej.grochowski.currencyapi.domain.Money;
import org.springframework.stereotype.Service;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

@Service
@AllArgsConstructor
public class CalculationService {

    private final RedirectionService redirectionService;
    private final BigDecimal MARGIN_MULTIPLIER = BigDecimal.valueOf(1.02);

    public BigDecimal customToForeignCurrency(String sellCurrency, String buyCurrency) {
        if (sellCurrency.equals("PLN")) {
            BigDecimal askRate = redirectionService.getRateOutOfCurrency(buyCurrency).getAsk();
            return BigDecimal.ONE.divide((askRate.multiply(MARGIN_MULTIPLIER)), 4, RoundingMode.HALF_UP);
        } else {
            BigDecimal bidRate = redirectionService.getRateOutOfCurrency(sellCurrency).getBid();
            return bidRate.divide(MARGIN_MULTIPLIER, 4, RoundingMode.HALF_UP);
        }
    }

    public BigDecimal foreignToForeignCurrency(String sellCurrency, String buyCurrency) {
        BigDecimal bidRate = redirectionService.getRateOutOfCurrency(sellCurrency).getBid();
        BigDecimal bidPrice = bidRate.divide(MARGIN_MULTIPLIER, 4, RoundingMode.HALF_UP);

        BigDecimal askRate = redirectionService.getRateOutOfCurrency(buyCurrency).getAsk();
        return bidPrice.divide((askRate.multiply(MARGIN_MULTIPLIER)), 4, RoundingMode.HALF_UP);
    }

    public boolean isValidAmount(String amount) {
        try {
            BigDecimal bigDecimalAmount = BigDecimal.valueOf(Double.parseDouble(amount));
            return bigDecimalAmount.scale() <= 2 && bigDecimalAmount.compareTo(BigDecimal.ZERO) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isValidCurrency(String currency1, String currency2, Class<CurrencyType> currencyEnum) {
        boolean isFirstEnum = Arrays.stream(currencyEnum.getEnumConstants()).anyMatch(cur -> cur.name().equals(currency1));
        boolean isSecondEnum = Arrays.stream(currencyEnum.getEnumConstants()).anyMatch(cur -> cur.name().equals(currency2));
        return isFirstEnum && isSecondEnum;
    }
}
