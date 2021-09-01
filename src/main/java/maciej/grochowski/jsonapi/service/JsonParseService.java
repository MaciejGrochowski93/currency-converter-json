package maciej.grochowski.jsonapi.service;

import lombok.AllArgsConstructor;
import maciej.grochowski.jsonapi.currency.Currency;
import maciej.grochowski.jsonapi.domain.Money;
import maciej.grochowski.jsonapi.domain.Rates;
import maciej.grochowski.jsonapi.exception.IncorrectCurrency;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class JsonParseService {

    private final RestTemplate restTemplate;

    public final static String CHF = "https://api.nbp.pl/api/exchangerates/rates/c/chf/?format=json";
    public final static String EUR = "https://api.nbp.pl/api/exchangerates/rates/c/eur/?format=json";
    public final static String GBP = "https://api.nbp.pl/api/exchangerates/rates/c/gbp/?format=json";

    private final static BigDecimal MARGIN_MULTIPLY = BigDecimal.valueOf(0.98);

    public Money parse(String URL) {
        return restTemplate.getForObject(URL, Money.class);
    }

    public String calculateForeignCurrencies(String sell, String buy) {
        String sellCurrency = chooseForeignCurrency(sell);
        Money sellMoney = parse(sellCurrency);
        List<Rates> bidRates = sellMoney.getRates();
        BigDecimal bid = bidRates.get(0).getBid();

        String buyCurrency = chooseForeignCurrency(buy);
        Money buyMoney = parse(buyCurrency);
        List<Rates> askRates = buyMoney.getRates();
        BigDecimal ask = askRates.get(0).getAsk();

        BigDecimal exchangeResult = bid.divide(ask, 4, RoundingMode.HALF_UP).multiply(MARGIN_MULTIPLY);
        return ("The exchange rate between " + sellMoney.getCode() + " and " + buyMoney.getCode() + " is: " + exchangeResult);
    }

    private String chooseForeignCurrency(String currency) {
        switch (currency) {
            case "EUR":
                return EUR;
            case "CHF":
                return CHF;
            case "GBP":
                return GBP;
            default:
                throw new IncorrectCurrency("Invalid currency.");
        }
    }

    public boolean areCurrenciesEnumMembers(String name, String name2, Class<Currency> currencyEnum) {
        boolean b1 = Arrays.stream(currencyEnum.getEnumConstants()).anyMatch(currency -> currency.name().equals(name));
        boolean b2 = Arrays.stream(currencyEnum.getEnumConstants()).anyMatch(currency -> currency.name().equals(name2));
        return b1 && b2;
    }
}
