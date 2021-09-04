package maciej.grochowski.currencyapi.controller;

import lombok.AllArgsConstructor;
import maciej.grochowski.currencyapi.currency.CurrencyType;
import maciej.grochowski.currencyapi.service.CalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static maciej.grochowski.currencyapi.currency.CurrencyType.PLN;

@RestController
@AllArgsConstructor
public class MoneyController {

    private final CalculationService calcService;

    @GetMapping("/{bid}/{amount}/{ask}")
    public ResponseEntity<String> exchangeValues(@PathVariable CurrencyType bid, @PathVariable BigDecimal amount, @PathVariable CurrencyType ask) {
        calcService.validateAmount(amount);
        if (ask.equals(bid)) {
            return ResponseEntity.badRequest().body("You cannot exchange a Currency with itself.");
        }
        if (ask == PLN || bid == PLN) {
            return ResponseEntity.ok(amount + " " + bid + " can be exchanged for " +
                    calcService.customToForeignCurrency(bid, ask).multiply(amount) + " " + ask);
        }
        return ResponseEntity.ok(amount + " " + bid + " can be exchanged for " +
                calcService.foreignToForeignCurrency(bid, ask).multiply(amount) + " " + ask);
    }
}