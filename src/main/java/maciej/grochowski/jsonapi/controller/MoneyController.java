package maciej.grochowski.jsonapi.controller;

import lombok.AllArgsConstructor;
import maciej.grochowski.jsonapi.currency.Currency;
import maciej.grochowski.jsonapi.domain.Money;
import maciej.grochowski.jsonapi.service.JsonParseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static maciej.grochowski.jsonapi.service.JsonParseService.CHF;

@RestController
@AllArgsConstructor
public class MoneyController {

    private final JsonParseService service;

    @GetMapping("/{bid}/{ask}")
    public ResponseEntity<String> CHFRateEuro(@PathVariable String bid, @PathVariable String ask) {
        if (service.areCurrenciesEnumMembers(ask, bid, Currency.class)) {
            if (bid.equals(ask)) {
                return new ResponseEntity<>("You cannot exchange a Currency with itself.", HttpStatus.BAD_REQUEST);
            } else if (ask.equals("PLN")) {
                return new ResponseEntity<>("You cannot convert to PLN", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(service.calculateForeignCurrencies(bid, ask), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Incorrect parameter", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/chf")
    public Money CHFRate() {
        return service.parse(CHF);
    }
}
