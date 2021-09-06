package maciej.grochowski.currencyapi.service;

import lombok.AllArgsConstructor;
import maciej.grochowski.currencyapi.currency.CurrencyType;
import maciej.grochowski.currencyapi.domain.Money;
import maciej.grochowski.currencyapi.domain.Rates;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class NBPService {

    private final RestTemplate restTemplate;

    public Money parseToMoney(String URL) {
        return restTemplate.getForObject(URL, Money.class);
    }

    public Rates getRateOutOfCurrency(CurrencyType providedCurrency) {
        if (providedCurrency == CurrencyType.PLN) {
            return new Rates(BigDecimal.ONE, BigDecimal.ONE);
        }

        String currencyLink = providedCurrency.getInformationLink();
        Money money = parseToMoney(currencyLink);
        return money.getRates().get(0);
    }
}
