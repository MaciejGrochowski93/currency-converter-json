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

    public Rates getRateOutOfCurrency(CurrencyType currencyType) {
        String currency = currencyType.getInformationLink();
        Money money = parseToMoney(currency);
        return money.getRates().get(0);
    }
}
