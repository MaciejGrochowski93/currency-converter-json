package maciej.grochowski.currencyapi.service;

import lombok.AllArgsConstructor;
import maciej.grochowski.currencyapi.currency.CurrencyType;
import maciej.grochowski.currencyapi.domain.Money;
import maciej.grochowski.currencyapi.domain.Rates;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class RedirectionService {

    private final RestTemplate restTemplate;

    public Money parseToMoney(String URL) {
        return restTemplate.getForObject(URL, Money.class);
    }

    public Rates getRateOutOfCurrency(String providedCurrency) {
        String currency = getLinkForForeignCurrency(providedCurrency);
        Money money = parseToMoney(currency);
        return money.getRates().get(0);
    }

    private String getLinkForForeignCurrency(String currency) {
        switch (currency) {
            case "EUR":
                return "https://api.nbp.pl/api/exchangerates/rates/c/eur/?format=json";
            case "CHF":
                return "https://api.nbp.pl/api/exchangerates/rates/c/chf/?format=json";
            default:
                return "https://api.nbp.pl/api/exchangerates/rates/c/gbp/?format=json";
        }
    }
}
