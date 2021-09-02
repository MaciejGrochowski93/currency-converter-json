package maciej.grochowski.jsonapi.service;

import lombok.AllArgsConstructor;
import maciej.grochowski.jsonapi.currency.CurrencyType;
import maciej.grochowski.jsonapi.domain.Money;
import maciej.grochowski.jsonapi.domain.Rates;
import maciej.grochowski.jsonapi.exception.IncorrectCurrency;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

@Service
@AllArgsConstructor
public class MoneyService {

    private final RestTemplate restTemplate;

    public final static String CHF = "https://api.nbp.pl/api/exchangerates/rates/c/chf/?format=json";
    public final static String EUR = "https://api.nbp.pl/api/exchangerates/rates/c/eur/?format=json";
    public final static String GBP = "https://api.nbp.pl/api/exchangerates/rates/c/gbp/?format=json";

    private final BigDecimal MARGIN_MULTIPLIER = BigDecimal.valueOf(1.02);

    public Money parseToMoney(String URL) {
        return restTemplate.getForObject(URL, Money.class);
    }

    public BigDecimal customToForeignCurrency(String sellCurrency, String buyCurrency) {
        if (sellCurrency.equals("PLN")) {
            BigDecimal askRate = getRateOutOfCurrency(buyCurrency).getAsk();
            return BigDecimal.ONE.divide((askRate.multiply(MARGIN_MULTIPLIER)), 4, RoundingMode.HALF_UP);
        } else {
            BigDecimal bidRate = getRateOutOfCurrency(sellCurrency).getBid();
            return bidRate.divide(MARGIN_MULTIPLIER, 4, RoundingMode.HALF_UP);
        }
    }

    public BigDecimal foreignToForeignCurrency(String sellCurrency, String buyCurrency) {
        BigDecimal bidRate = getRateOutOfCurrency(sellCurrency).getBid();
        BigDecimal bidPrice = bidRate.divide(MARGIN_MULTIPLIER, 4, RoundingMode.HALF_UP);

        BigDecimal askRate = getRateOutOfCurrency(buyCurrency).getAsk();
        return bidPrice.divide((askRate.multiply(MARGIN_MULTIPLIER)), 4, RoundingMode.HALF_UP);
    }

    private Rates getRateOutOfCurrency(String providedCurrency) {
        String currency = getLinkForForeignCurrency(providedCurrency);
        Money money = parseToMoney(currency);
        return money.getRates().get(0);
    }

    private String getLinkForForeignCurrency(String currency) {
        switch (currency) {
            case "EUR":
                return EUR;
            case "CHF":
                return CHF;
            case "GBP":
                return GBP;
            default:
                throw new IncorrectCurrency("Invalid foreign currency.");
        }
    }

    public boolean areCurrenciesEnums(String currency1, String currency2, Class<CurrencyType> currencyEnum) {
        boolean isFirstEnum = Arrays.stream(currencyEnum.getEnumConstants()).anyMatch(cur -> cur.name().equals(currency1));
        boolean isSecondEnum = Arrays.stream(currencyEnum.getEnumConstants()).anyMatch(cur -> cur.name().equals(currency2));
        return isFirstEnum && isSecondEnum;
    }
}
