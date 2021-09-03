package maciej.grochowski.currencyapi.controller;

import lombok.AllArgsConstructor;
import maciej.grochowski.currencyapi.currency.CurrencyType;
import maciej.grochowski.currencyapi.service.CalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Arrays;

@RestController
@AllArgsConstructor
public class MoneyController {

    private final CalculationService calcService;

    @GetMapping("/{bid}/{amount}/{ask}")
    public ResponseEntity<String> exchangeValues(@PathVariable String bid, @PathVariable String amount, @PathVariable String ask) {
        if (calcService.isValidCurrency(ask, bid, CurrencyType.class) && calcService.isValidAmount(amount)) {
            if (ask.equals(bid)) {
                return ResponseEntity.badRequest().body("You cannot exchange a Currency with itself.");
            } else if (ask.equals("PLN") || bid.equals("PLN")) {
                return ResponseEntity.ok(amount + " " + bid + " can be exchanged for " +
                        calcService.customToForeignCurrency(bid, ask).multiply(BigDecimal.valueOf(Double.parseDouble(amount))) + " " + ask);
            }
            return ResponseEntity.ok(amount + " " + bid + " can be exchanged for " +
                    calcService.foreignToForeignCurrency(bid, ask).multiply(BigDecimal.valueOf(Double.parseDouble(amount))) + " " + ask); // amount
        }
        if (!calcService.isValidAmount((amount))) {
            return ResponseEntity.badRequest().body("Please, provide positive number with up to 2 decimal places.");
        }
        return ResponseEntity.badRequest().body("Please, provide correct currencies: "
                + Arrays.toString(Arrays
                .stream(CurrencyType.values())
                .toArray()));
    }
}

