package maciej.grochowski.currencyapi.currency;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum CurrencyType {
    PLN,
    EUR("https://api.nbp.pl/api/exchangerates/rates/c/eur/?format=json"),
    CHF("https://api.nbp.pl/api/exchangerates/rates/c/chf/?format=json"),
    GBP("https://api.nbp.pl/api/exchangerates/rates/c/gbp/?format=json");

    private String informationLink;

    public static List<CurrencyType> availableCurrencies() {
        return List.of(CurrencyType.values());
    }

//    private final static Map<String, CurrencyType> myMap;
//
//    static {
//        myMap = Arrays.stream(values())
//                .collect(Collectors.toMap(CurrencyType::name, Function.identity(), (currentOne, newOne) -> currentOne));
//    }
//
//    @JsonCreator
//    public static CurrencyType getCurrency(String name) {
//        return Optional.ofNullable(myMap
//                .get(name))
//                .orElseThrow(CurrencyNotFound::new
//                );
//    }
//    // mapa

    @Override
    public String toString() {
        return this.name();
    }
}
