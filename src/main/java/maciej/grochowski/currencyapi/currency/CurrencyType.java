package maciej.grochowski.currencyapi.currency;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum CurrencyType {
    PLN,
    EUR("https://api.nbp.pl/api/exchangerates/rates/c/eur/?format=json"),
    CHF("https://api.nbp.pl/api/exchangerates/rates/c/chf/?format=json"),
    GBP("https://api.nbp.pl/api/exchangerates/rates/c/gbp/?format=json");

    private String informationLink;

    @Override
    public String toString() {
        return this.name();
    }
}
