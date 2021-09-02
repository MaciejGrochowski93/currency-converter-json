package maciej.grochowski.jsonapi.controller;

import lombok.AllArgsConstructor;
import maciej.grochowski.jsonapi.currency.CurrencyType;
import maciej.grochowski.jsonapi.service.MoneyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Arrays;

@RestController
@AllArgsConstructor
public class MoneyController {

    private final MoneyService service;

    @GetMapping("/{bid}/{amount}/{ask}")
    public ResponseEntity<String> ExchangeValues(@PathVariable String bid, @PathVariable BigDecimal amount, @PathVariable String ask) {
        if (service.areCurrenciesEnums(ask, bid, CurrencyType.class)) {
            if (ask.equals(bid)) {
                return ResponseEntity.badRequest().body("You cannot exchange a Currency with itself.");
            } else if (ask.equals("PLN") || bid.equals("PLN")) {
                return ResponseEntity.ok(amount + " " + bid + " can be exchanged for " + service.customToForeignCurrency(bid, ask).multiply(amount) + " " + ask);
            }
            return ResponseEntity.ok(amount + " " + bid + " can be exchanged for " + service.foreignToForeignCurrency(bid, ask).multiply(amount) + " " + ask);
        }
        return ResponseEntity.badRequest().body("Please, provide correct currencies: "
                + Arrays.toString(Arrays.stream(CurrencyType.values()).toArray()));
    }
}
